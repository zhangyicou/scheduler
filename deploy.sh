#!/bin/bash
#
# 将该文件放在任意的目录下，执行即可。
# dest 要用绝对路径
#

FILE_NAME_DEPLOY="ibd-scheduler.jar"
SRC_DIR="src"
GIT_USER="liurui"
GIT_PWD="12345678"

main() {

  # 源码路径（Base）
  if [[ "${srcBaseDir}" == "" ]]; then
    echo "source base dir is null"
    return 1
  fi  

  # Jar文件名
  if [[ "${jarFile}" == "" ]]; then
    echo "jar file is null, please input."
    return 1
  fi

  # 发布目的路径
  if [[ "${destDir}" == "" ]]; then
    echo "dest dir is null, please input."
    return 1
  fi

  # 发布环境
  if [[ "${deployEnv}" == "" ]]; then
    echo "deploy env is null, please input."
    return 1
  fi

  if [[ ! -d "${destDir}" ]]; then
    echo "dest dir is not exist : ${destDir}"
    mkdir "${destDir}"
    echo "mkdir ${destDir}"
  fi

  if [[ ! -d "${destDir}" ]]; then
    echo "dest dir is not exist : ${destDir}"
    return 1
  fi

  # src路径不存在，则创建。
  if [[ ! -d "${SRC_DIR}" ]]; then
    mkdir "${SRC_DIR}"
    echo "mkdir : ${SRC_DIR} ==> $?"
  fi

  if [[ ! -d "${SRC_DIR}" ]]; then
    echo "${SRC_DIR} is not exist"
    return 1
  fi

  cd "${SRC_DIR}"

  if [[ ${gitClone} -eq 1 ]]; then
      # 重新下载代码
      rm -rf "${srcBaseDir}"
      echo "rm -rf ${srcBaseDir} ==> $?"

      git clone "http://${GIT_USER}:${GIT_PWD}@192.168.93.5/data/ibd-scheduler.git"
      echo "git clone http://${GIT_USER}:${GIT_PWD}@192.168.93.5/data/ibd-scheduler.git"
  fi

  pwd

  if [[ ! -d "${srcBaseDir}" ]]; then
    echo "source base dir not exist: ${srcBaseDir}"
    return 1
  fi

  cd "${srcBaseDir}"
  echo "cd ${srcBaseDir} ==> $?"

  if [[ ${gitBranch} != "" ]]; then
      git checkout "${gitBranch}"
      echo "git checkout ${gitBranch}"
  fi

  if [[ ${gitPull} -eq 1 ]]; then
      git pull
      echo "git pull"
  fi

  pwd

  mvn clean package -P "${deployEnv}"
  echo "mvn clean package -P ${deployEnv}"

  jarfileCompiled="target/${jarFile}"
  if [[ ! -f "${jarfileCompiled}" ]]; then
    echo "maybe compile error, jar file compiled is not exist. ${jarfileCompiled}"
    return 1
  fi

  # 不等于1时，仅编译打包。
  if [[ ${deployAndRun} -ne 1 ]]; then
    echo "return by only compile and package"
    return 0
  fi  

  handleDate=$(date +%Y%m%d_%H%M%S)

  dest="${destDir}/${FILE_NAME_DEPLOY}"

  if [[ -f "${dest}" ]]; then
    mv "${dest}" "${dest}.${handleDate}"
    echo "mv ${dest} ${dest}.${handleDate}"
  fi

  cp "${jarfileCompiled}" "${dest}"
  echo "cp ${jarfileCompiled} ${dest} ==> $?"

  chmod +x "${dest}"
  echo "chmod +x ${dest} ==> $?"

  # 查找pid
  pid=`ps -aux |grep "java" |grep "${FILE_NAME_DEPLOY}" |awk '{print $2}'`
  echo "ps -aux |grep \"java\" |grep ${FILE_NAME_DEPLOY} |awk '{print \$2}'"
  echo "pid=${pid}"

  if [[ "${pid}" != "" ]]; then
      kill -9 "${pid}"
      echo "kill -9 ${pid} ==> $?"
  fi

  #运行
  #java -jar "${dest}" "--spring.profiles.active=${deployEnv}" &
  java -Xmx600m -Xms600m -Xss256k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dat/log/ibd-scheduler/dump -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/dat/log/ibd-scheduler/heap_trace.txt -Dcom.sun.management.jmxremote=true -Djava.rmi.server.hostname=192.168.93.10 -Dcom.sun.management.jmxremote.port=8191 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar "${dest}" "--spring.profiles.active=${deployEnv}" &
  echo "java -Xmx600m -Xms600m -Xss256k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dat/log/ibd-scheduler/dump -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/dat/log/ibd-scheduler/heap_trace.txt -jar ${dest} --spring.profiles.active=${deployEnv} &"

}

# src目录下代码的具体目录
srcBaseDir=$1
echo "srcBaseDir: ${srcBaseDir}"

# 编译后target下jar文件的名字
jarFile=$2
echo "jarFile: ${jarFile}"

# 程序的发布目录
destDir=$3
echo "destDir: ${destDir}"

# 发布的环境
deployEnv=$4
echo "deployEnv: ${deployEnv}"

# 发布并运行 0：仅编译 1：发布并运行
deployAndRun=$5
echo "deployAndRun: ${deployAndRun}"

# 从git更新代码
gitClone=$6
echo "gitClone: ${gitClone}"

# 从git更新代码
gitBranch=$7
echo "gitBranch: ${gitBranch}"

# 从git更新代码
gitPull=$8
echo "gitPull: ${gitPull}"

main

# 编译 运行 clone master pull
# ./deploy.sh "ibd-scheduler" "ibd-scheduler-1.0.0-SNAPSHOT.jar" "/dat/java/ibd-scheduler/app" "dev" 1(run) 1(git clone) "master"(checkout branch) 1(git pull)
# ./deploy.sh "ibd-scheduler" "ibd-scheduler-1.0.0-SNAPSHOT.jar" "/dat/java/ibd-scheduler/app" "dev" 1 1 "master" 1
# ./deploy.sh "ibd-scheduler" "ibd-scheduler-1.0.0-SNAPSHOT.jar" "/dat/java/ibd-scheduler/app" "dev" 1 1 "dev" 1
# ./deploy.sh "ibd-scheduler" "ibd-scheduler-1.0.0-SNAPSHOT.jar" "/dat/java/ibd-scheduler/app" "dev" 1 1 "1.0.0" 1

# 不运行
# ./deploy.sh "ibd-scheduler" "ibd-scheduler-1.0.0-SNAPSHOT.jar" "/dat/java/ibd-scheduler/app" "dev" 0 1 "dev" 1