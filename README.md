# 進捗メーター

## 概要

タスクの進捗率を視覚的に管理できるToDoアプリです。

タブでタスクをカテゴリ別に整理し、各タスクの完了数・合計数から進捗率をドーナツグラフで確認できます。

## 機能

- **Googleアカウントでログイン** (OAuth2)
- **タブ管理**: タスクをカテゴリ（タブ）ごとに整理
- **タスク管理**: タスクの作成・編集・削除
- **進捗管理**: 完了数と合計数を入力して進捗率を管理
- **進捗の可視化**: 進捗率をドーナツグラフで表示

## 技術スタック

| カテゴリ | 技術 |
| --- | --- |
| 言語 | Java 25 |
| フレームワーク | Spring Boot 3.5.7 |
| DB アクセス | Spring Data JDBC |
| 認証 | Spring Security 6 + Google OAuth2 |
| セッション | Spring Session JDBC |
| データベース | PostgreSQL |
| テンプレートエンジン | Thymeleaf |
| フロントエンド | HTML / CSS / Vanilla JavaScript (ES Modules) |
| ビルドツール | Maven |
