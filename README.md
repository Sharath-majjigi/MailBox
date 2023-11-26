# MailBox
A secure Email parody app where users can connect and officially talk with their Github friends !

![Springboot](https://img.shields.io/badge/springboot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![ApacheCassandra](https://img.shields.io/badge/cassandra-%231287B1.svg?style=for-the-badge&logo=apache-cassandra&logoColor=white)

# Checkout the demo here
[Mailbox-demo](https://www.veed.io/view/aa17ce0c-afa0-47b9-bcaa-f985f02bd11b?panel=share)

# MailBox - Design Document

## Index:
1. [Functional Requirements](#functional-requirements)
2. [Non-Functional Requirements](#non-functional-requirements)
3. [Database Design](#database-design)
4. [High Level Diagram](#high-level-diagram)
5. [APIs](#apis-to-build)
6. [Tech Stack Used](#tech-stack-used)

## Functional Requirements:

1. **Compose a New Message:**
   - Users can create and compose new messages.

2. **Send a Message by User ID:**
   - Users can send messages to other users by specifying their IDs.

3. **View List of Messages:**
   - Display a list of all sent and received messages.

4. **Message Folders:**
   - Categorize messages into folders (Received, Sent, Important, User-Defined Folders).

5. **Reply to Messages:**
   - Users can reply to individual messages or reply to all users of a specific message.

6. **View Single Message:**
   - Users can view a single message from the list.

## Non-Functional Requirements:

1. **High Availability:**
   - Ensure the system is highly available to users.

2. **High Scalability:**
   - Design for scalability to handle a growing user base.

3. **Authentication:**
   - Implement user authentication for secure access.

## Database Design:

**Details:**
- PK (Partition Key)
- CK (Clustering Key)
  
![DB Design](https://i.ibb.co/SvQ4b9V/image2.png)


## High Level Diagram:
![HLD](https://i.ibb.co/R7KqtNf/image1.png)

## APIs to Build:

1. `GET /homepage`
2. `GET /composeMessage`
3. `POST /sendMessage`
4. `GET /messageView`
5. `POST /replyMessage`
6. `POST /replyAll`

## Tech Stack Used:

- Spring Boot
- ThymeLeaf (No fancy frontend framework for a focus on building a highly scalable backend)
- Spring Security
- Apache Cassandra
- Git (Version Control System)
- IntelliJ IDEA (IDE)


## How to Use

## Step 1: Clone this repository:

```sh
git clone https://github.com/Sharath-majjigi/MailBox
```

## Step 2: Set Up the Cassandra Database

1. Create an account in [Astra DB](https://astra.datastax.com/register).
2. Follow [this tutorial](https://spring.io/guides/gs/accessing-data-cassandra/) to connect the database to the application.
3. Update the `application.yml` file with the keys generated during the setup.


## Step 3: Log In with Your GitHub Account

Use your GitHub credentials to log in to the application.

## Step 4: Compose and Send a Message

1. Enter the GitHub username(s) in the "To" address.
2. Add a subject and body to your message.
3. Click "Submit" to send your message!


Thats it ! Your friend who have an account will have unread message in his inbox now he can reply to it or compose seperate message.

