# 0001 — Choose PostgreSQL instead of MongoDB

## 1. Before

The old version uses MongoDB. Reason at that time: read that MongoDB "suitable for blogging" and
"easy to use for modern web apps", without actual data analysis before selection.

## 2. Reason

The Blog data model has explicit relationships and needs referential integrity: User 1-N Post,
Post N-N Tag, Post 1-N Comment, Post N-1 Catagory.
Use NoSQL for data with a strong relation
structure that requires manual processing consistency at the application layer instead of having
the DB ensure it (foreign key, transaction).
In additional, the goal of learning SQL/index/query plan in the next version (V3) requires RDBMS.

## 3. Solution

Choose PostgreSQL. Design standardized schema (3NF) for User, Post, Comment, Catagory, Tag.
Use Flyway to manage schema versioning from the start.
