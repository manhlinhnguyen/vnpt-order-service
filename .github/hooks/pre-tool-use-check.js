#!/usr/bin/env node
/**
 * pre-tool-use-check.js
 * Hook chạy trước khi GHCP agent thực thi lệnh terminal.
 * Đọc input từ stdin (JSON), kiểm tra lệnh, in kết quả ra stdout.
 *
 * Dùng trong .github/hooks/pre-tool-use.json
 * Demo cho §7.8 của tài liệu đào tạo GitHub Copilot.
 */

const BLOCKED_PATTERNS = [
  { pattern: /git\s+push\s+.*--force/, reason: "git push --force bị chặn — dùng MR trên GitLab" },
  { pattern: /git\s+push\s+.*-f\b/, reason: "git push -f bị chặn — dùng MR trên GitLab" },
  { pattern: /rm\s+-rf\s+\//, reason: "rm -rf với đường dẫn tuyệt đối bị chặn" },
  { pattern: /DROP\s+TABLE/i, reason: "DROP TABLE bị chặn — dùng migration script" },
  { pattern: /DROP\s+DATABASE/i, reason: "DROP DATABASE bị chặn" },
  { pattern: /git\s+reset\s+--hard/, reason: "git reset --hard cần xác nhận thủ công" },
  { pattern: /git\s+commit\s+.*--amend.*--no-edit/, reason: "amend published commit bị chặn" },
];

let input = "";
process.stdin.on("data", (chunk) => { input += chunk; });
process.stdin.on("end", () => {
  let command = "";
  try {
    const data = JSON.parse(input);
    command = data?.toolInput?.command || data?.command || "";
  } catch {
    command = input.trim();
  }

  for (const { pattern, reason } of BLOCKED_PATTERNS) {
    if (pattern.test(command)) {
      console.log(JSON.stringify({
        action: "block",
        message: `🔒 [VNPT Hook] Lệnh bị chặn: ${reason}\nLệnh: ${command}`
      }));
      process.exit(0);
    }
  }

  console.log(JSON.stringify({ action: "allow" }));
  process.exit(0);
});
