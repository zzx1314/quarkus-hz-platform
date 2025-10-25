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
def parse_private_fields_with_pre_comments(java_file_path):
    """
    解析 Java 实体类文件，提取 private 字段及备注（备注来源于字段前的单行注释）
    :param java_file_path: Java 文件路径
    :return: 字段对象列表 [{'name': 'id', 'comment': 'id'}, {'name': 'nodes', 'comment': '节点'}, ...]
    """
    with open(java_file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    fields = []
    prev_comment = ""
    for line in lines:
        line = line.strip()
        # 忽略空行
        if not line:
            prev_comment = ""
            continue
        # 匹配单行注释
        comment_match = re.match(r'//\s*(.*)', line)
        if comment_match:
            prev_comment = comment_match.group(1).strip()
            continue
        # 匹配 private 字段（排除 static 或 final）
        field_match = re.match(r'private\s+(?!static|final)[\w<>?,\s\[\]]+\s+(\w+)\s*(=[^;]*)?;', line)
        if field_match:
            field_name = field_match.group(1)
            fields.append({
                "name": field_name,
                "comment": prev_comment
            })
            prev_comment = ""  # 用完就清空
    return fields

# -------------------- 渲染生成 --------------------
def generate_file(template_name: str, file_suffix: str, data: dict, gen_path: str):
    """
    根据模板生成文件
    :param template_name: 模板 key
    :param file_suffix: 文件后缀
    :param data: 渲染数据
    :param gen_path: 生成的子目录，例如 'entity/dto'
    """
    # 加载模板
    template = env.get_template(TEMPLATES[template_name])

    # 解析实体类字段
    entity_java_path = os.path.join(
        JAVA_ROOT_DIR,
        package_path,
        "entity",
        f"{data['entity_name']}.java"
    )
    fields = parse_private_fields_with_pre_comments(entity_java_path)
    data['fields'] = fields

    # 渲染内容
    output = template.render(data)

    # 包路径
    package_path = data["base_package_name"].replace('.', os.sep)

    # 拼接最终生成目录：JAVA_ROOT_DIR/package_path/gen_path
    full_dir = os.path.join(JAVA_ROOT_DIR, package_path, gen_path)
    os.makedirs(full_dir, exist_ok=True)

    # 文件路径
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
        generate_file('dto', 'Dto.java', data, "entity/dto")
        generate_file('query_dto', 'QueryDto.java', data, "entity/dto")
        generate_file('mapper', 'Mapper.java', data, "entity/mapper")
        generate_file('repository', 'Repository.java', data, "repository")
        generate_file('service', 'Service.java', data, "service")
        generate_file('serviceImp', 'ServiceImp.java', data, "service")
        generate_file('controller', 'Controller.java', data, "controller")
        if arg == "1":
            print("后端代码生成完成！")

    # 生成前端
    if arg == "2" or arg is None:
        generate_file('frontend', 'index.html', data, "frontend")
        generate_file('foreApi', 'fore_api.tsx', data, "frontend")
        generate_file('foreForm', 'fore_form.tsx', data, "frontend")
        generate_file('foreHook', 'fore_hook.tsx', data, "frontend")
        generate_file('foreIndex', 'fore_index.vue', data, "frontend")
        if arg == "2":
            print("前端代码生成完成！")

    if arg is None:
        print("前端和后端代码生成完成！")

