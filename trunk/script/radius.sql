/*
SQLyog v4.05
Host - 4.1.7-nt : Database - radius
*********************************************************************
Server version : 4.1.7-nt
*/


create database if not exists `radius`;

USE `radius`;

/*Table structure for table `radius`.`account` */

drop table if exists `radius`.`account`;

CREATE TABLE `account` (
  `id` bigint(20) NOT NULL default '0',
  `user_name` varchar(16) default NULL,
  `status_type` int(11) default NULL,
  `delay_time` int(11) default NULL,
  `input_octects` bigint(20) default NULL,
  `output_octectes` bigint(20) default NULL,
  `session_id` varchar(50) default NULL,
  `authentic` int(11) default NULL,
  `sessionTime` int(11) default NULL,
  `input_packets` int(11) default NULL,
  `output_packets` int(11) default NULL,
  `terminate_cause` int(11) default NULL,
  `multi_session_id` varchar(50) default NULL,
  `link_count` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`group_and_service_type` */

drop table if exists `radius`.`group_and_service_type`;

CREATE TABLE `group_and_service_type` (
  `id` int(11) NOT NULL default '0',
  `group_id` int(11) NOT NULL default '0',
  `service_type_id` int(11) NOT NULL default '0',
  `value` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`nas` */

drop table if exists `radius`.`nas`;

CREATE TABLE `nas` (
  `ip` varchar(20) NOT NULL default '',
  `secret` varchar(255) NOT NULL default '',
  `vendor_name` varchar(32) default NULL,
  PRIMARY KEY  (`ip`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`proxy` */

drop table if exists `radius`.`proxy`;

CREATE TABLE `proxy` (
  `id` int(11) NOT NULL default '0',
  `from_ip` varchar(20) NOT NULL default '',
  `to_ip` varchar(20) NOT NULL default '',
  `type` varchar(10) NOT NULL default '',
  `from_port` int(11) default NULL,
  `to_port` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`service_type` */

drop table if exists `radius`.`service_type`;

CREATE TABLE `service_type` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `type` varchar(16) NOT NULL default '',
  `att_id` int(11) NOT NULL default '0',
  `unit` varchar(16) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`user` */

drop table if exists `radius`.`user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL default '0',
  `name` varchar(16) NOT NULL default '',
  `password` varchar(16) NOT NULL default '',
  `encrypt` char(1) NOT NULL default 'N',
  `auth_method` varchar(10) NOT NULL default 'PAP',
  `status` char(1) NOT NULL default 'C',
  `login_count` int(11) NOT NULL default '0',
  `group_id` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`user_group` */

drop table if exists `radius`.`user_group`;

CREATE TABLE `user_group` (
  `id` int(11) NOT NULL default '0',
  `name` varchar(50) NOT NULL default '',
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `radius`.`vendor` */

drop table if exists `radius`.`vendor`;

CREATE TABLE `vendor` (
  `name` varchar(32) NOT NULL default '',
  `id` int(11) NOT NULL default '0',
  `type` varchar(16) NOT NULL default '',
  PRIMARY KEY  (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
