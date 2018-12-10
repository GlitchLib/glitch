#!/usr/bin/env bash

./gradlew javadoc

JAVADOC_PATH=./build/javadocs
JAVADOC_REPO=https://${GITHUB_TOKEN}@github.com/GlitchLib/docs.git
JAVADOC_DESTINATION=${JAVADOC_PATH}/${TRAVIS_TAG}

git clone ${JAVADOC_REPO} ${JAVADOC_PATH}

mkdir ${JAVADOC_DESTINATION}

for PROJECT in "auth" "chat" "core" "docs" "helix" "kraken" "pubsub"; do
    DIRECTORY=${PROJECT}/build/docs/javadoc

    if [[ PROJECT -eq "docs" ]]; then
        PROJECT="all"
    fi

    cp -ar ${DIRECTORY} ${JAVADOC_DESTINATION}/${PROJECT}
done

cd ${JAVADOC_PATH}

cp -L ./latest/index.html ./${TRAVIS_TAG}/index.html
ln -sfn ./${TRAVIS_TAG} ./latest

git add .
git commit -m "Build release: ${TRAVIS_TAG}"
git push origin master

