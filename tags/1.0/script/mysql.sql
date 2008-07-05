DROP DATABASE radius;
create DATABASE radius;
use radius;
CREATE TABLE RADIUS_USER (
	id int auto_increment,
	name VARCHAR (16),
	password VARCHAR (16) NOT NULL, 
	encrypt CHAR (1) DEFAULT 'N',
	authMethod varchar(32) DEFAULT 'PAP',
	PRIMARY KEY(id), UNIQUE(name), INDEX(name)
);

CREATE TABLE RADIUS_USER_DATA (
	name1  varchar(16),
	value1  varchar(16),
	name2  varchar(16),
	value2  varchar(16),
	name3  varchar(16),
	value3  varchar(16),
	name4  varchar(16),
	value4  varchar(16),
	name5  varchar(16),
	value5  varchar(16),
	name6  varchar(16),
	value6  varchar(16),
	name7  varchar(16),
	value7  varchar(16),
	name8  varchar(16),
	value8  varchar(16),
	name9  varchar(16),
	value9  varchar(16),
	name10  varchar(16),
	value10  varchar(16),
	name11  varchar(16),
	value11  varchar(16),
	name12  varchar(16),
	value12  varchar(16),
	name13  varchar(16),
	value13  varchar(16),
	name14  varchar(16),
	value14  varchar(16),
	name15  varchar(16),
	value15  varchar(16),
	name16  varchar(16),
	value16  varchar(16),
	name17  varchar(16),
	value17  varchar(16),
	name18  varchar(16),
	value18  varchar(16),
	name19  varchar(16),
	value19  varchar(16),
	name20  varchar(16),
	value20  varchar(16),
	name21  varchar(16),
	value21  varchar(16),
	name22  varchar(16),
	value22  varchar(16),
	name23  varchar(16),
	value23  varchar(16),
	name24  varchar(16),
	value24  varchar(16),
	name25  varchar(16),
	value25  varchar(16),
	name26  varchar(16),
	value26  varchar(16),
	name27  varchar(16),
	value27  varchar(16),
	name28  varchar(16),
	value28  varchar(16),
	name29  varchar(16),
	value29  varchar(16),
	name30  varchar(16),
	value30  varchar(16),
	name31  varchar(16),
	value31  varchar(16),
	name32  varchar(16),
	value32  varchar(16),
	name33  varchar(16),
	value33  varchar(16),
	name34  varchar(16),
	value34  varchar(16),
	name35  varchar(16),
	value35  varchar(16),
	name36  varchar(16),
	value36  varchar(16),
	name37  varchar(16),
	value37  varchar(16),
	name38  varchar(16),
	value38  varchar(16),
	name39  varchar(16),
	value39  varchar(16),
	name40  varchar(16),
	value40  varchar(16),
	name41  varchar(16),
	value41  varchar(16),
	name42  varchar(16),
	value42  varchar(16),
	name43  varchar(16),
	value43  varchar(16),
	name44  varchar(16),
	value44  varchar(16),
	name45  varchar(16),
	value45  varchar(16),
	name46  varchar(16),
	value46  varchar(16),
	name47  varchar(16),
	value47  varchar(16),
	name48  varchar(16),
	value48  varchar(16),
	name49  varchar(16),
	value49  varchar(16),
	name50  varchar(16),
	value50  varchar(16)
);
 
CREATE TABLE RADIUS_VENDOR (
	vendorName VARCHAR(32) NOT NULL,
	vendorId int NOT NULL,
	vendorType varchar(16),
	PRIMARY KEY(vendorName)
);

CREATE TABLE RADIUS_ATTRIBUTE (
	id int auto_increment,
	vendorName VARCHAR(32) NOT NULL,
	attName VARCHAR(64) NOT NULL,
	attId int NOT NULL,
	attType VARCHAR(32) NOT NULL,
	PRIMARY KEY(id), INDEX(attName)
);

CREATE TABLE RADIUS_VALUE (
	id int auto_increment,
	attName VARCHAR(64) NOT NULL,
	valueName VARCHAR(64) NOT NULL,
	valueEnumName VARCHAR(64) NOT NULL,
	value int,
	PRIMARY KEY(id)
);

CREATE TABLE RADIUS_SERVICE_TYPE (
	name varchar(32),
	type varchar(10),
	attId int,
	unit varchar(10),
	PRIMARY KEY(name)
);

CREATE TABLE RADIUS_ACCOUNT (
	id bigint auto_increment,
	userName varchar(16),
	statusType varchar(10),
	delayTime int,
	inputOctects bigint,
	outputOctectes bigint,
	sessionId varchar(50),
	authentic varchar(10),
	sessionTime int,
	inputPackets int,
	outputPacket int,
	terminateCause varchar(20),
	multiSessionId varchar(50),
	linkCount int,
	PRIMARY KEY(id)
);

CREATE TABLE NAS (
	vendorName varchar(16),
	ip varchar(20),
	secret varchar(250),
	PRIMARY KEY(ip)
);
