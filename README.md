# Blog Backend API

🚀 A production-ready, scalable REST API backend for [blog.pengunin.me](https://blog.pengunin.me) built with modern Spring Boot architecture. Features comprehensive blog management, JWT authentication, and professional-grade security.

> **Live Demo**: [blog.pengunin.me](https://blog.pengunin.me) | **Frontend**: [blog-fe repository](https://github.com/penguninn/blog-fe)

[![CI/CD](https://github.com/penguninn/blog-be/actions/workflows/ci.yml/badge.svg)](https://github.com/penguninn/blog-be/actions/workflows/ci.yml)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)

## ✨ Key Features

- 🔐 **Secure Authentication**: JWT-based auth with Keycloak integration
- 📝 **Rich Content Management**: Full CRUD operations for posts, categories
- 🖼️ **Asset Management**: Image upload with Cloudinary integration
- 📊 **Post Engagement**: Like/unlike functionality and comment system
- 📱 **RESTful API**: OpenAPI 3.0 documented endpoints
- 🐳 **Production**: Docker containerization

## 🏗️ Architecture & Tech Stack

### Backend Technologies
```
Java 21              Spring Boot 3.4.4      
Spring Security 6    Keycloak 26.3.0        Cloudinary
```

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Git

### 🏃‍♂️ Development Setup
```bash
# 1. Clone the repository
git clone https://github.com/penguninn/blog-be.git
cd blog-be

# 2. Start infrastructure services
docker-compose -f docker-compose.dev.yml up -d

# 3. Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at `http://localhost:8080`

### 🐳 Production Deployment
```bash
# Build and run with Docker
docker build -t blog-api .
docker run -p 8080:8080 --env-file .env blog-api
```

## 📁 Project Architecture

```
blog-be/
├── 🏗️ src/main/java/com/daviddai/blog/
│   ├── 🔧 config/              # Spring configurations
│   ├── 🎮 controller/          # REST API endpoints
│   │   ├── PostController.java        # Blog post operations
│   │   ├── AssetController.java       # File upload/management
│   │   ├── CommentController.java     # Comment system
│   │   └── UserController.java       # User management
│   ├── 📦 dto/                 # Data transfer objects
│   ├── 🗃️ entity/              
│   ├── 🔄 mapper/              # MapStruct mappers
│   ├── 📊 repository/          # Data access layer
│   └── ⚙️ service/             # Business logic
├── 🐳 Docker ecosystem
│   ├── Dockerfile              # Production container
│   └── docker-compose.dev.yml  # Development stack
└── 🔨 Build & Config
    ├── pom.xml                 # Maven dependencies
```

## ⚙️ Configuration

### Environment Variables

Create a `.env` file based on `.env.template`:

```env
# Database
MONGODB_URI=mongodb://localhost:27017/blog_db
MONGODB_DATABASE=blog_db

# Keycloak
KEYCLOAK_REALM=blog
KEYCLOAK_CLIENT_ID=blog-api
KEYCLOAK_AUTH_SERVER_URL=http://localhost:9000

# Application
SERVER_PORT=8081
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://blog.penguninn.com

# Cloudinary (Asset Management)
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

### Application Profiles

- **`dev`**: Development with detailed logging and hot reload
- **`prod`**: Production optimized settings

## 📚 API Documentation

### Base URL
```
Production: https://api.blog.penguninn.com/api
Development: http://localhost:8080/api
```

### Authentication
Include JWT token in Authorization header:
```http
Authorization: Bearer <jwt-token>
```

### 📖 Interactive Documentation0
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:80810/v3/api-docs`

### Core Endpoints

#### 📝 Posts Management
```http
GET    /api/posts              # Get all posts (paginated)
GET    /api/posts/{id}         # Get post by ID
GET    /api/posts/slug/{slug}  # Get post by slug
POST   /api/posts              # Create new post (Admin only)
PUT    /api/posts/{id}         # Update post (Admin only)
DELETE /api/posts/{id}         # Delete post (Admin only)
```

#### 📂 Categories & Comments
```http
GET    /api/categories         # Get all categories
POST   /api/categories         # Create category (Admin)
GET    /api/posts/{postId}/comments  # Get post comments
POST   /api/posts/{postId}/comments  # Add comment
```

#### 🖼️ Asset Management
```http
POST   /api/assets/upload      # Upload image/file (Admin)
```

#### 👤 Post Engagement
```http
POST   /api/posts/{postId}/like    # Like/unlike post
GET    /api/posts/{postId}/likes   # Get like count
```

### Request Examples

#### Create Post
```json
{
  "title": "Advanced Spring Boot Tips",
  "slug": "advanced-spring-boot-tips",
  "excerpt": "Learn professional Spring Boot development patterns",
  "content": "Full post content here...",
  "status": "PUBLISHED",
  "categoryId": "60f4d2e5c8e8b12a3c456789",
  "published": true
}
```

### Response Format
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {...},
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## 🔒 Security & Permissions

### Access Levels
- **Admin (`ROLE_ADMIN`)**: Full access to all endpoints
- **User (`ROLE_USER`)**: Read access + comment/like functionality  
- **Anonymous**: Read-only access to published content

### Features
- JWT token validation
- Role-based authorization
- CORS protection
- Request validation
- Rate limiting (production)


## 📞 Support & Contact

### 🔗 Links
- **Live Site**: [blog.penguninn.com](https://blog.penguninn.com)
- **Frontend Repo**: [github.com/penguninn/blog-fe](https://github.com/penguninn/blog-fe)
- **Issues**: [Report bugs/feature requests](../../issues)
- **Discussions**: [Community support](../../discussions)

### 👨‍💻 Maintainer
**PenguNinn**
- GitHub: [@penguninn](https://github.com/penguninn)
- Website: [penguninn.com](https://pengunin.me)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

**Built with ❤️ using Spring Boot 3.x and modern Java**
