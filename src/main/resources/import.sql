insert into CONTACT (ID, PUBLICID, FIRSTNAME, LASTNAME, PRIMARYEMAIL, PHONENUMBER) values (1, 'public:1', 'Erika', 'Mustermann', 'erik@must.er', '+49 11833' );
insert into USER (ID, PUBLICID, SYSTEMUSER, CONTACT_ID) values (1, 'public:1', false, 1);
insert into CREDENTIALS (ID, PUBLICID, USER_ID, USERNAME) values (1, 'public:1', 1, 'emustermann');
