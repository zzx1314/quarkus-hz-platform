#!/usr/bin/env python3
"""
Git Log Viewer with Fuzzy Search
查看某个文件的git提交记录和修改内容，支持模糊查询文件名
"""

import os
import sys
import subprocess
import argparse
from typing import List, Tuple, Optional
from pathlib import Path


class GitLogViewer:
    def __init__(self, repo_path: str = "."):
        self.repo_path = Path(repo_path).resolve()
        if not self._is_git_repo():
            raise ValueError(f"Not a git repository: {self.repo_path}")
    
    def _is_git_repo(self) -> bool:
        """检查是否为git仓库"""
        return (self.repo_path / ".git").exists()
    
    def _run_git_command(self, cmd: List[str]) -> str:
        """执行git命令并返回结果"""
        try:
            result = subprocess.run(
                ["git"] + cmd,
                cwd=self.repo_path,
                capture_output=True,
                text=True,
                check=True
            )
            return result.stdout.strip()
        except subprocess.CalledProcessError as e:
            return f"Error: {e.stderr.strip()}"
    
    def fuzzy_find_files(self, pattern: str) -> List[str]:
        """
        模糊查询文件
        :param pattern: 文件名模式
        :return: 匹配的文件路径列表
        """
        if not pattern:
            return []
        
        # 使用git ls-files获取仓库中的所有文件
        try:
            cmd_result = self._run_git_command(["ls-files"])
            all_files = cmd_result.split('\n') if cmd_result else []
        except Exception:
            # 如果git命令失败，使用find命令
            all_files = []
            for root, dirs, files in os.walk(self.repo_path):
                if '.git' in root:
                    continue
                for file in files:
                    rel_path = os.path.relpath(os.path.join(root, file), self.repo_path)
                    all_files.append(rel_path)
        
        # 模糊匹配
        pattern_lower = pattern.lower()
        matched_files = []
        
        for file_path in all_files:
            file_name = os.path.basename(file_path).lower()
            dir_name = os.path.dirname(file_path).lower()
            
            # 匹配文件名或路径
            if (pattern_lower in file_name or 
                pattern_lower in file_path.lower() or
                pattern_lower in dir_name):
                matched_files.append(file_path)
        
        return sorted(matched_files)
    
    def get_file_git_log(self, file_path: str, max_count: int = 20) -> List[Tuple[str, str, str]]:
        """
        获取文件的git提交记录
        :param file_path: 文件路径
        :param max_count: 最大提交数量
        :return: 提交记录列表 (hash, author, message)
        """
        cmd = [
            "log",
            f"--max-count={max_count}",
            "--pretty=format:%H|%an|%s",
            "--",
            file_path
        ]
        
        result = self._run_git_command(cmd)
        
        if result.startswith("Error:"):
            return []
        
        commits = []
        for line in result.split('\n'):
            if line.strip():
                parts = line.split('|', 2)
                if len(parts) >= 3:
                    commits.append((parts[0], parts[1], parts[2]))
        
        return commits
    
    def get_file_diff(self, file_path: str, commit_hash: str) -> str:
        """
        获取文件在特定提交的修改内容
        :param file_path: 文件路径
        :param commit_hash: 提交hash
        :return: diff内容
        """
        cmd = [
            "show",
            f"{commit_hash}:{file_path}"
        ]
        
        try:
            current_content = self._run_git_command(cmd)
            if current_content.startswith("Error:"):
                return "File not found in this commit"
        except Exception:
            current_content = ""
        
        # 获取父提交的内容
        parent_cmd = [
            "show",
            f"{commit_hash}^:{file_path}"
        ]
        
        try:
            parent_content = self._run_git_command(parent_cmd)
            if parent_content.startswith("Error:"):
                parent_content = ""
        except Exception:
            parent_content = ""
        
        # 生成diff
        if parent_content == current_content:
            return "No changes in this commit"
        
        # 使用git diff生成标准diff格式
        diff_cmd = [
            "diff",
            f"{commit_hash}^",
            commit_hash,
            "--",
            file_path
        ]
        
        diff_result = self._run_git_command(diff_cmd)
        if diff_result.startswith("Error:"):
            return "Could not generate diff"
        
        return diff_result
    
    def show_file_history(self, pattern: str, show_diff: bool = False, max_count: int = 20):
        """
        显示文件历史记录
        :param pattern: 文件名模式
        :param show_diff: 是否显示diff
        :param max_count: 最大显示数量
        """
        # 查找匹配的文件
        matched_files = self.fuzzy_find_files(pattern)
        
        if not matched_files:
            print(f"❌ 未找到匹配 '{pattern}' 的文件")
            return
        
        if len(matched_files) > 1:
            print(f"🔍 找到 {len(matched_files)} 个匹配的文件:")
            for i, file_path in enumerate(matched_files, 1):
                print(f"  {i}. {file_path}")
            
            try:
                choice = input("\n请选择文件序号 (回车选择第1个): ").strip()
                if not choice:
                    choice = "1"
                file_index = int(choice) - 1
                if 0 <= file_index < len(matched_files):
                    selected_file = matched_files[file_index]
                else:
                    print("❌ 无效选择")
                    return
            except (ValueError, KeyboardInterrupt):
                print("\n❌ 操作取消")
                return
        else:
            selected_file = matched_files[0]
        
        print(f"\n📄 文件: {selected_file}")
        print("=" * 80)
        
        # 获取提交历史
        commits = self.get_file_git_log(selected_file, max_count)
        
        if not commits:
            print("❌ 未找到该文件的提交记录")
            return
        
        print(f"📝 提交历史 (最近 {len(commits)} 条):")
        print("-" * 80)
        
        for i, (commit_hash, author, message) in enumerate(commits, 1):
            print(f"\n{i}. {commit_hash[:8]} - {author}")
            print(f"   {message}")
            
            if show_diff:
                print(f"\n   📋 修改内容:")
                diff_content = self.get_file_diff(selected_file, commit_hash)
                
                # 限制diff显示长度
                if len(diff_content) > 1000:
                    diff_content = diff_content[:1000] + "\n... (内容过长，已截断)"
                
                # 缩进显示diff
                for line in diff_content.split('\n'):
                    print(f"   {line}")
                
                print("-" * 60)


def main():
    parser = argparse.ArgumentParser(
        description="查看文件的git提交记录和修改内容，支持模糊查询",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
示例用法:
  python gitlog.py SysUser              # 查找包含'SysUser'的文件
  python gitlog.py user.java -d         # 查找文件并显示修改内容
  python gitlog.py controller -n 10     # 显示最近10条提交
  python gitlog.py /path/to/file        # 使用完整路径
        """
    )
    
    parser.add_argument(
        "pattern",
        help="文件名模式或完整路径"
    )
    
    parser.add_argument(
        "-d", "--diff",
        action="store_true",
        help="显示修改内容 (diff)"
    )
    
    parser.add_argument(
        "-n", "--max-count",
        type=int,
        default=20,
        help="最大显示提交数量 (默认: 20)"
    )
    
    parser.add_argument(
        "-r", "--repo-path",
        default=".",
        help="git仓库路径 (默认: 当前目录)"
    )
    
    args = parser.parse_args()
    
    try:
        viewer = GitLogViewer(args.repo_path)
        viewer.show_file_history(args.pattern, args.diff, args.max_count)
    except Exception as e:
        print(f"❌ 错误: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()