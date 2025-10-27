from jinja2 import Environment, FileSystemLoader
import os
import re
import sys

# -------------------- 配置 --------------------
TEMPLATE_DIR = '../templates/'  # 模板根目录
JAVA_ROOT_DIR = '/home/zhangzexin/IdeaProjects/hz_server/src/main/java'  # Java 文件根目录

# 定义模板和对应生成文件后缀
TEMPLATES = {
    'dto': 'dto.java.j2',
    'query_dto': 'querydto.java.j2',
    'mapper': 'mapper.java.j2',
    'repository': 'repository.java.j2',
    'service': 'service.java.j2',
    'serviceImp': 'serviceImp.java.j2',
    'controller': 'controller.java.j2',
    'foreApi': 'fore_api.ts.j2',
    'foreForm': 'fore_form.tsx.j2',
    'foreHook': 'fore_hook.tsx.j2',
    'foreIndex': 'fore_index.vue.j2',
}

# 数据模型---修改
data = {
    "base_package_name": "org.hzai.drones.command", #修改
    "entity_name": "DronesCommand" #修改
}

# -------------------- 初始化 Jinja2 --------------------
env = Environment(loader=FileSystemLoader(TEMPLATE_DIR))

# -------------------- 提取java实体类 --------------------
def parse_private_fields_with_javadoc(java_file_path):
    """
    解析 Java 实体类文件，只提取 private 字段及前置多行注释（Javadoc）
    :param java_file_path: Java 文件路径
    :return: 字段对象列表 [{'name': 'routeName', 'comment': '路线名称'}, ...]
    """
    with open(java_file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    fields = []
    prev_comment_lines = []
    in_multiline_comment = False

    for line in lines:
        line = line.strip()
        if not line:
            continue
        # 多行注释开始 /** 或 /*
        if line.startswith("/**") or line.startswith("/*"):
            in_multiline_comment = True
            prev_comment_lines = []
            # 如果注释在同一行结束 */
            if line.endswith("*/") and len(line) > 4:
                comment_text = line[3:-2].strip()
                prev_comment_lines = [comment_text]
                in_multiline_comment = False
            continue

        # 多行注释内容
        if in_multiline_comment:
            if line.endswith("*/"):
                comment_line = line[:-2].strip().lstrip('*').strip()
                if comment_line:
                    prev_comment_lines.append(comment_line)
                in_multiline_comment = False
            else:
                comment_line = line.strip().lstrip('*').strip()
                if comment_line:
                    prev_comment_lines.append(comment_line)
            continue

        # 匹配 private 字段（排除 static 或 final）
        field_match = re.match(r'private\s+(?!static|final)[\w<>?,\s\[\]]+\s+(\w+)\s*(=[^;]*)?;', line)
        if field_match:
            field_name = field_match.group(1)
            comment = " ".join(prev_comment_lines).strip() if prev_comment_lines else ""
            fields.append({
                "name": field_name,
                "comment": comment
            })
            prev_comment_lines = []  # 用完清空

    return fields

# -------------------- 渲染生成 --------------------
def generate_file(template_name: str, file_suffix: str, data: dict, gen_path: str, type: str):
    """
    根据模板生成文件
    :param template_name: 模板 key
    :param file_suffix: 文件后缀
    :param data: 渲染数据
    :param gen_path: 生成的子目录，例如 'entity/dto'
    """
    # 加载模板
    template = env.get_template(TEMPLATES[template_name])

    # 渲染内容
    output = template.render(data)

    # 包路径
    package_path = data["base_package_name"].replace('.', os.sep)

    # 解析实体类字段
    entity_java_path = os.path.join(
        JAVA_ROOT_DIR,
        package_path,
        "entity",
        f"{data['entity_name']}.java"
    )
    fields = parse_private_fields_with_javadoc(entity_java_path)
    data['fields'] = fields


    # 拼接最终生成目录：JAVA_ROOT_DIR/package_path/gen_path
    full_dir = os.path.join(JAVA_ROOT_DIR, package_path, gen_path)
    os.makedirs(full_dir, exist_ok=True)

    # 文件路径
    if type == "2":
        file_path = os.path.join(full_dir, f"{file_suffix}")
    elif type == "1":
        file_path = os.path.join(full_dir, f"{data['entity_name']}{file_suffix}")
    else:
        file_path = os.path.join(full_dir, f"{data['entity_name']}{file_suffix}")

    # 输出文件
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(output)

    print(f"[生成] {file_path}")
    return file_path

# -------------------- 执行生成 --------------------
if __name__ == "__main__":
    # 获取命令行参数
    arg = sys.argv[1] if len(sys.argv) > 1 else None

    # 生成后端
    if arg == "1" or arg is None:
        generate_file('dto', 'Dto.java', data, "entity/dto", "1")
        generate_file('query_dto', 'QueryDto.java', data, "entity/dto", "1")
        generate_file('mapper', 'Mapper.java', data, "entity/mapper", "1")
        generate_file('repository', 'Repository.java', data, "repository", "1")
        generate_file('service', 'Service.java', data, "service", "1")
        generate_file('serviceImp', 'ServiceImp.java', data, "service", "1")
        generate_file('controller', 'Controller.java', data, "controller", "1")
        if arg == "1":
            print("后端代码生成完成！")

    # 生成前端
    if arg == "2" or arg is None:
        generate_file('foreApi', f"{data['entity_name'][0].lower() + data['entity_name'][1:]}.ts", data, "frontend/api", "2")
        generate_file('foreForm', 'form.tsx', data, "frontend", "2")
        generate_file('foreHook', 'hook.tsx', data, "frontend", "2")
        generate_file('foreIndex', 'index.vue', data, "frontend", "2")
        if arg == "2":
            print("前端代码生成完成！")

    if arg is None:
        print("前端和后端代码生成完成！")

