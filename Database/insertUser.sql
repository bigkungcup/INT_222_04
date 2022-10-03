SELECT @@system_time_zone; 

use clinic;

insert into user (name,email,role)
value ('สมส่วน สุขศรี','somsuan.s@kmutt.ac.th','lecturer');

insert into user (name,email,role)
value ('0001-0002-0003-0004-0005-0006-0007-0008-0009-0010-0011-0012-0013-0014-0015-0016-0017-0018-0019-0020-',
'thisismyverylongtest.email@0123456789.kmutt.ac.th','student');

insert into user (name,email,role)
value ('สดใส มาสาย','sodsai.m@kmutt.ac.th','admin');

insert into user (name,email,role)
value ('0001-0002-0003-0004-0005-0006-0007-0008-0009-0010-0011-0012-0013-0014-0015-0016-0017-0018-0019-0020-!',
'thisismyverylongtest.emaill@0123456789.kmutt.ac.th','guest');

insert into user (name,email,role)
value ('สดใส ไม่สาย','sodsai.m@kmutt@ac.th','admin');
select * from user;

delete from user where userId=8;