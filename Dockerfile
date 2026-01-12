# Java 17 の軽量イメージを使用
FROM eclipse-temurin:17-jdk

# コンテナ内の作業ディレクトリ
WORKDIR /app

# プロジェクトファイルをすべてコピー
COPY . .

# 改行コードのトラブルを防ぐため実行権限を付与し、ビルド（jar作成）
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# 実行するjarファイルを指定（targetフォルダ内に生成されたもの）
# ※ファイル名が異なる場合は target フォルダ内を確認してください
CMD ["java", "-jar", "target/instacopy-0.0.1-SNAPSHOT.jar"]
