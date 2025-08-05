@echo off
echo Compiling all Java files in src...
mkdir out 2>nul
for /R src %%f in (*.java) do (
  echo Compiling %%f
  javac -cp "src" -d out "%%f"
)
echo Done!
