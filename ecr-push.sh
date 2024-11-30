#!/bin/bash

# ECRリポジトリ名
REPO_NAME="$1"
if [ -z "$REPO_NAME" ]; then
  echo "ECRリポジトリ名を指定してください。"
  exit 1
fi
REPO_TAG="$2"
if [ -z "$REPO_TAG" ]; then
  echo "ECRリポジトリのタグを指定してください。"
  exit 1
fi
ORIGIN_IMAGE="$3"
if [ -z "$ORIGIN_IMAGE" ]; then
  echo "ORIGIN_IMAGEを指定してください。"
  exit 1
fi

REGION="ap-northeast-1"
export AWS_DEFAULT_PROFILE=terraform

# リポジトリの存在を確認
EXISTING_REPO=$(aws ecr describe-repositories \
  --repository-names $REPO_NAME \
  --region $REGION \
  --query "repositories[?repositoryName=='$REPO_NAME'] | [0].repositoryName" \
  --output text 2>/dev/null)

echo "EXISTING_REPO: $EXISTING_REPO"

if [ "$EXISTING_REPO" == "" ]; then
  echo "リポジトリ $REPO_NAME は存在しません。作成を実行します..."
  aws ecr create-repository \
    --repository-name $REPO_NAME \
    --region $REGION
  echo "リポジトリ $REPO_NAME を作成しました。"
else
  echo "リポジトリ $REPO_NAME は既に存在します。"
fi

AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com

docker tag $ORIGIN_IMAGE $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO_NAME:$REPO_TAG

docker push $AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO_NAME:$REPO_TAG