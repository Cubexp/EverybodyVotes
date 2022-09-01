/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : everybody_votes

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 25/05/2021 00:02:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_news
-- ----------------------------
DROP TABLE IF EXISTS `sys_news`;
CREATE TABLE `sys_news`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `create_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '新闻状态 己发布，草稿',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_news_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_news_type`;
CREATE TABLE `sys_news_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号码',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称:管理员，用户',
  `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别',
  `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `source` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台名称',
  `uuid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台uuid',
  `register_time` bigint(20) NULL DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_phone_uindex`(`phone`) USING BTREE,
  UNIQUE INDEX `user_email_uindex`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_activity
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_activity`;
CREATE TABLE `sys_user_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL COMMENT '活动id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `number` bigint(20) NULL DEFAULT NULL COMMENT '编号',
  `views` bigint(15) NULL DEFAULT NULL COMMENT '浏量',
  `review` tinyint(2) NULL DEFAULT NULL COMMENT '审核是否通知',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '分组id',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '参加时间',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '选手封面图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for votes_activity
-- ----------------------------
DROP TABLE IF EXISTS `votes_activity`;
CREATE TABLE `votes_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '活动是否开启',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动标题',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `views` bigint(20) NULL DEFAULT NULL COMMENT '浏览量',
  `begin_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NOT NULL COMMENT '结束时间',
  `rule_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动内容',
  `video_cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频封面',
  `notice_flag` tinyint(2) NULL DEFAULT NULL COMMENT '活动公告',
  `notice` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告内容',
  `sign_flag` tinyint(2) NULL DEFAULT NULL COMMENT '活动开启选手是否可以报名',
  `player_method` tinyint(2) NULL DEFAULT NULL COMMENT '选手排序方式',
  `sing_up_flag` tinyint(2) NULL DEFAULT NULL COMMENT '报名审核',
  `vote_rule_type` tinyint(2) NULL DEFAULT NULL COMMENT '投票规则',
  `create_id` bigint(20) NOT NULL COMMENT '创建者id',
  `cover_type` tinyint(2) NOT NULL COMMENT '封面类型',
  `vote_button_type` tinyint(2) NOT NULL COMMENT '投票按钮类型 分数还是点击',
  `input_range_begin` double(10, 0) NULL DEFAULT NULL COMMENT '如果为分数，设置分数的范围',
  `input_range_end` double(10, 0) NULL DEFAULT NULL,
  `group_flag` tinyint(2) NULL DEFAULT NULL COMMENT '分组是否开启',
  `player_vote_count` int(10) NOT NULL COMMENT '选手投票数量',
  `secret_vote_flag` tinyint(2) NOT NULL COMMENT '是否开启匿名投票',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for votes_activity_grouping
-- ----------------------------
DROP TABLE IF EXISTS `votes_activity_grouping`;
CREATE TABLE `votes_activity_grouping`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分组名称',
  `activity_id` bigint(20) NULL DEFAULT NULL COMMENT '活动id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for votes_activity_picture
-- ----------------------------
DROP TABLE IF EXISTS `votes_activity_picture`;
CREATE TABLE `votes_activity_picture`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `img_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片url',
  `activity_id` bigint(20) NULL DEFAULT NULL COMMENT '活动id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图片封面' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for votes_activity_votes
-- ----------------------------
DROP TABLE IF EXISTS `votes_activity_votes`;
CREATE TABLE `votes_activity_votes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vote_person_id` bigint(20) NULL DEFAULT NULL COMMENT '投票人id',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `activity_id` bigint(20) NOT NULL COMMENT '活动id',
  `create_time` bigint(20) NULL DEFAULT NULL COMMENT '时间',
  `value` double(3, 0) NOT NULL COMMENT '值',
  `user_activity_id` bigint(20) NOT NULL COMMENT '投票对象id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
