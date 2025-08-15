from jinja2 import Environment, FileSystemLoader
import os

# -------------------- 配置 --------------------
TEMPLATE_DIR = '../templates/'  # 模板根目录
JAVA_ROOT_DIR = '/home/zzx/IdeaProjects/CleverCopilot/src/main/java'  # Java 文件根目录

# 定义模板和对应生成文件后缀
TEMPLATES = {
    'dto': 'dto.java.j2',
    'query_dto': 'querydto.java.j2',
}

# 数据模型
data = {
    "base_package_name": "org.hzai.system.sysmenu",
    "entity_name": "SysMenu"
}

# -------------------- 初始化 Jinja2 --------------------
env = Environment(loader=FileSystemLoader(TEMPLATE_DIR))

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
    # DTO 文件
    generate_file('dto', 'Dto.java', data, "entity/dto")

    # QueryDTO 文件
    generate_file('query_dto', 'QueryDto.java', data, "entity/dto")
