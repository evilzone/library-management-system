# library-management-system
spring boot application for library management

======================================================================================================================

As a User
I can sign up either as LIBRARIAN and MEMBER using username and password

- hit http://localhost:8080/ in the browser
- it will open the register page for (LIBRARIAN and MEMBER)
- input the feed and click on submit button to create an account.
- on successful creation, token will be displayed in the browser.

<img width="1440" alt="Screenshot 2023-05-09 at 23 23 30" src="https://github.com/evilzone/library-management-system/assets/1716829/4d06bae4-dda5-4ca5-869d-60758e80d7db">

<img width="1438" alt="Screenshot 2023-05-09 at 23 21 58" src="https://github.com/evilzone/library-management-system/assets/1716829/202f8adb-3075-486a-8f1c-eff3a01ebffd">

I can login using username/password and get JWT access token

- hit http://localhost:8080/loginuser in the browser
- feed the credententials for the registered user
- on successful authentication, it will proceed to book management and user management pages
- for LIBRARIAN, it will render librarian.html
- for MEMBER, it will render member.html

<img width="1439" alt="Screenshot 2023-05-09 at 23 25 26" src="https://github.com/evilzone/library-management-system/assets/1716829/19711ff0-4a74-454f-86a8-2d8bec485d68">

<img width="1439" alt="Screenshot 2023-05-09 at 23 28 42" src="https://github.com/evilzone/library-management-system/assets/1716829/d3657995-7a97-41a1-a346-800c18e7afea">


======================================================================================================================

As a Librarian

I can add, update, and remove Books from the system

- hit http://localhost:8080/loginuser in the browser
- login with Librarian account to proceed to page to add update and remove books from the system 

<img width="1440" alt="Screenshot 2023-05-09 at 23 31 36" src="https://github.com/evilzone/library-management-system/assets/1716829/fb64b8ad-eed6-4aa1-a75a-615640abfd7a">

I can add, update, view, and remove Member from the system

- hit http://localhost:8080/loginuser in the browser
- login with Librarian account to proceed to page to add update and remove members from the system 


======================================================================================================================
As a Member

I can view, borrow, and return available Books
Once a book is borrowed, its status will change to BORROWED
Once a book is returned, its status will change to AVAILABLE
I can delete my own account

- - hit http://localhost:8080/loginuser in the browser
- login with Member account to proceed to page to view, borrow, return books from the system 

<img width="1440" alt="Screenshot 2023-05-09 at 23 35 26" src="https://github.com/evilzone/library-management-system/assets/1716829/02e4f9dc-660a-4aeb-9c74-f4c05cac9834">
