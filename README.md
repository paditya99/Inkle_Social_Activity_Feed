# Social Media Activity Feed

## Project Overview

The **Social Media Activity Feed** project is a backend application designed to facilitate user interactions in a social media environment. Built using **Java**, **Spring Boot**, and **PostgreSQL**, this application allows users to create profiles, manage posts, follow other users, and engage with content through likes and comments. The application also includes administrative functionalities for managing user roles and activities.

## Key Features

- **User Management**:
  - User Registration: Users can create their profiles by registering with a username, email, and password.
  - User Login: Users can log in to their accounts to access the application.
  - Profile Management: Users can view and update their profile information.

- **Post Management**:
  - Create Posts: Users can create posts to share updates, thoughts, or media with their followers.
  - Like Posts: Users can like posts made by others, indicating their appreciation or interest.
  - Delete Posts: Users can delete their own posts if they wish to remove them from the feed.

- **Social Interactions**:
  - Follow Users: Users can follow other users to see their activities and posts in their feed.
  - Unfollow Users: Users can unfollow others if they no longer wish to see their updates.
  - Block Users: Users can block other users, preventing them from seeing their posts and activities.

- **Activity Feed**:
  - The application maintains an activity wall where users can see updates about their network, such as new posts, follows, and likes. For example:
    - "ABC made a post"
    - "DEF followed ABC"
    - "PQR liked ABC's post"

- **Admin and Owner Functionalities**:
  - Admin Role: Admins can delete user profiles, likes, and posts, ensuring the platform remains clean and free of inappropriate content.
  - Owner Role: The owner has all admin capabilities and can promote or demote other users to/from admin roles.

## API Endpoints

### User Management Endpoints

- **POST /api/users/register**: Registers a new user with a username, email, and password.
- **POST /api/auth/login**: Authenticates a user and returns a JWT token for session management.
- **POST /api/users/{userId}/follow**: Allows the authenticated user to follow another user by their ID.
- **POST /api/users/{userId}/block**: Blocks a user, preventing them from seeing the authenticated user's posts.
- **DELETE /api/users/{userId}**: Deletes a user profile (admin functionality).

### Post Management Endpoints

- **POST /api/posts**: Creates a new post for the authenticated user.
- **GET /api/posts**: Retrieves all posts visible to the authenticated user, excluding posts from blocked users.
- **POST /api/posts/{postId}/like**: Likes a specific post by its ID.
- **POST /api/posts/{postId}/unlike**: Unlikes a specific post by its ID.
- **DELETE /api/posts/{postId}**: Deletes a specific post (admin functionality).

### Admin Functionality Endpoints

- **POST /api/admin/users/{userId}/promote**: Promotes a user to admin status.
- **POST /api/admin/users/{userId}/demote**: Demotes an admin back to a regular user.
- **DELETE /api/admin/users/{userId}**: Deletes a user profile (admin functionality).
- **DELETE /api/admin/posts/{postId}**: Deletes a specific post (admin functionality).

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- PostgreSQL

### Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/your-repo-name.git
   cd your-repo-name
   ```

2. **Configure Database**:
   - Create a PostgreSQL database and update the `application.properties` file in `src/main/resources` with your database credentials.

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`.

## Testing the Endpoints

You can test the API endpoints using Postman. Import the provided Postman collection for easy access to all endpoints.

### Import Postman Collection

1. Open Postman.
2. Click on "Import" in the top left corner.
3. Select the `InkleBackendProject.postman_collection.json` file.
4. Click "Import".

### Example Requests

- **Register User**:
  - Use the `/api/users/register` endpoint to create a new user.
  
- **Login User**:
  - Use the `/api/auth/login` endpoint to authenticate and receive a JWT token.

- **Follow User**:
  - Use the `/api/users/{userId}/follow` endpoint to follow another user.

## Conclusion

This project provides a robust backend for a social media application, allowing users to interact and manage their profiles effectively. For any issues or contributions, feel free to open an issue or submit a pull request.

---