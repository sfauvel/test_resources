
# Link current dir with container working dir
options="-v $(pwd):/documents/ -w=/documents"

# Build cucumber report and PDF with Tzatziki
docker run -t ${options} java:8u92-jre-alpine java  -jar scripts/cucumber2report.jar ./features

# Merge cucumber json files
docker run -t ${options} python:3.6 python scripts/cucumber_merge_json.py
# Transfom json file to asciidoctor
docker run -t ${options} python:3.6 python scripts/cucumber_json2adoc.py
# Generate HTML and PDF from asciidoctor
docker run -t ${options} asciidoctor/adoc scripts/generate_adoc.sh reports/cucumber.adoc
