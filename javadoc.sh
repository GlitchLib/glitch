#!/usr/bin/env bash

./gradlew javadoc

JAVADOC_REPO=https://github.com/GlitchLib/docs.git

git clone ${JAVADOC_REPO} ./build/javadocs

for PROJECT in "auth" "chat" "core" "docs" "helix" "kraken" "pubsub"; do
    DIRECTORY=${PROJECT}/build/docs/javadoc
    if [[ PROJECT == "docs" ]]; then
        PROJECT="all"
    fi

    JAVADOC_DESTINATION=build/javadocs/${TRAVIS_TAG}/${PROJECT}

    cp -ar ${DIRECTORY} ${JAVADOC_DESTINATION}
done

cd ./build/javadocs

ln -sfn ./${TRAVIS_TAG} ./latest
cp ./latest/index.html ./${TRAVIS_TAG}/index.html

git commit -m "Build release: ${TRAVIS_TAG}"
git push origin master
