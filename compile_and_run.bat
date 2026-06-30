@echo off
echo ========================================
echo 个人健康管理平台 - 编译和运行脚本
echo ========================================
echo.

echo 正在编译Java源文件...
javac -encoding UTF-8 -d bin src\model\*.java src\util\*.java src\dao\*.java src\service\*.java src\ui\*.java src\Main.java

if %errorlevel% neq 0 (
    echo 编译失败！请检查Java环境。
    pause
    exit /b 1
)

echo 编译成功！
echo.
echo 正在启动程序...
echo.

java -cp bin -Dfile.encoding=UTF-8 Main

pause