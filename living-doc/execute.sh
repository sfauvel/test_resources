FOLDER_TO_DOC=$(pwd)/src/test/java

# Link current dir with container working dir
options="-v ${FOLDER_TO_DOC}:/usr/src/to_doc -v $(pwd):/project/ -w=/project"

cp /usr/lib/jvm/java-1.8.0-openjdk-amd64/lib/tools.jar .

docker run -t ${options} java:8u92-jre-alpine java -classpath tools.jar:doclet_living_doc.jar doclet_living_doc.Rundoc
