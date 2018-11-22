#!/usr/bin/env bash

./gradlew javadoc

JAVADOC_REPO=https://github.com/GlitchLib/docs.git
JAVADOC_DESTINATION=build/javadocs/${TRAVIS_TAG}

git clone ${JAVADOC_REPO} ./build/javadocs

mkdir ${JAVADOC_DESTINATION}

for PROJECT in "auth" "chat" "core" "docs" "helix" "kraken" "pubsub"; do
    DIRECTORY=${PROJECT}/build/docs/javadoc
    if [[ PROJECT == "docs" ]]; then
        PROJECT="all"
    fi

    cp -ar ${DIRECTORY} ${JAVADOC_DESTINATION}/${PROJECT}
done

cd ./build/javadocs

cp ./latest/index.html ./${TRAVIS_TAG}/index.html
ln -sfn ./${TRAVIS_TAG} ./latest

git add .
git commit -m "Build release: ${TRAVIS_TAG}"
git push origin master

