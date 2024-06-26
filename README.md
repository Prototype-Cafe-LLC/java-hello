# java-hello
 
AWS lambda で Javaを実行するための Java/CDK設定と実行される lambda (Java) appを含む。

サンプルとしてlambdaを定期的に実行する。

## 必要な環境

ホストWindowsには [Docker Desktop](https://docs.docker.com/desktop/install/windows-install/) をインストールしてください。インストール後、`docker compose run --rm cdk_java` を実行するだけで、下記のパッケージが入った環境を利用できます。
- 2024/4現在、[企業規模により、Docker Desktopの利用は有償](https://www.docker.com/pricing/?_gl=1*1umk19r*_ga*NDIzMTM4OTE2LjE2NjYwNzg1MTE.*_ga_XJWPQMJYHQ*MTcxMjU3NTI2Ni4zLjEuMTcxMjU3Nzk4NS42MC4wLjA.)となります。

- Docker コンテナに含まれるパッケージ
  - [NodeJS (2024時点ではversion 20.*.* 推奨)](https://nodejs.org/en) - windows subsystem で [nvmを入れて](https://github.com/nvm-sh/nvm?tab=readme-ov-file#installing-and-updating)使うか、[nvm-windows](https://github.com/coreybutler/nvm-windows)を使ってインストールすることをお勧めします。保守・バージョン切り替えが楽になります。
  - [AWS CLI](https://docs.aws.amazon.com/ja_jp/cli/latest/userguide/getting-started-install.html) - AWSの個々のリソースの操作
  - [AWS CDK](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html#getting_started_install) - AWSリソースをまとめて構築
  - [Open JDK](https://adoptium.net/) または、 [Amazon Corretto](https://aws.amazon.com/jp/corretto/?filtered-posts.sort-by=item.additionalFields.createdDate&filtered-posts.sort-order=desc) - Javaの無償実行環境
  - [Apache Maven]( https://maven.apache.org/index.html )  - Javaのビルド環境。CDKはMarven projectsを作成します。

### ホストWindowsに必要な環境

編集を行う場合、無償の Visual Studio Codeなどを利用してください。
- (推奨) Visual Studio Code
- (推奨) Extension:: AWS Toolkit for VS Code
- (推奨) Extension:: Github Copilot
- (推奨) Extension:: Github Copilot Chat
- (推奨) Extension:: Extension Pack for Java

## 参考
- [JavaによるAWS CDKプロジェクト開発](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html)
  - [Working with the AWS CDK in Java](https://docs.aws.amazon.com/cdk/v2/guide/work-with-cdk-java.html)

### プロジェクトのセットアップ

`aws config` を実行するか、例として次のようなファイルを二つ作成

#### `~/.aws/config`
```
[profile __your_profile_name__]
region = ap-northeast-1
output = json
```

#### ``~/.aws/credentials`
```
[__your_profile_name__]
aws_access_key_id = xxxxxxxxxxxxxxxxx
aws_secret_access_key = xxxxxxxxxxxxxxxxxxx
```

必要な環境をいれたらプロジェクトのルートディレクトリでbootstrapを行いCDKプロジェクトの準備をする。(AWS側でCDKにより使用されるS3バケット, CloudFormationスタックが準備される)
```
# https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html#getting_started_bootstrap
export AWS_PROFILE=__your_profile_name__
cdk bootstrap aws://ACCOUNT-NUMBER/REGION
```

プロジェクト作成
```bash
mkdir app
cd app
cdk init app --language java
export CDK_DEFAULT_ACCOUNT=__your_aws_accont_id
export CDK_DEFAULT_REGION=__your_aws_region
```

必要なパッケージを https://mvnrepository.com/ で探して、marven用のdependency 定義を pom.xmlに加えてから
```
# 文字化けが発生する場合 less を使うと内容を確認できる
mvn compile | less
```