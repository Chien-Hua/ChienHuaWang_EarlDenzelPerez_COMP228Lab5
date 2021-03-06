USE [master]
GO
/****** Object:  Database [MyGame]    Script Date: 7/31/2018 4:22:08 PM ******/
CREATE DATABASE [MyGame]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'MyGame', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\MyGame.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'MyGame_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\MyGame_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [MyGame] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [MyGame].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [MyGame] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [MyGame] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [MyGame] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [MyGame] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [MyGame] SET ARITHABORT OFF 
GO
ALTER DATABASE [MyGame] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [MyGame] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [MyGame] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [MyGame] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [MyGame] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [MyGame] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [MyGame] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [MyGame] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [MyGame] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [MyGame] SET  DISABLE_BROKER 
GO
ALTER DATABASE [MyGame] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [MyGame] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [MyGame] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [MyGame] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [MyGame] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [MyGame] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [MyGame] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [MyGame] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [MyGame] SET  MULTI_USER 
GO
ALTER DATABASE [MyGame] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [MyGame] SET DB_CHAINING OFF 
GO
ALTER DATABASE [MyGame] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [MyGame] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [MyGame] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [MyGame] SET QUERY_STORE = OFF
GO
USE [MyGame]
GO
ALTER DATABASE SCOPED CONFIGURATION SET IDENTITY_CACHE = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION SET LEGACY_CARDINALITY_ESTIMATION = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET LEGACY_CARDINALITY_ESTIMATION = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 0;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET MAXDOP = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET PARAMETER_SNIFFING = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET PARAMETER_SNIFFING = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET QUERY_OPTIMIZER_HOTFIXES = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET QUERY_OPTIMIZER_HOTFIXES = PRIMARY;
GO
USE [MyGame]
GO
/****** Object:  Table [dbo].[Game]    Script Date: 7/31/2018 4:22:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Game](
	[game_id] [int] NOT NULL,
	[game_title] [nvarchar](50) NULL,
 CONSTRAINT [PK_Game] PRIMARY KEY CLUSTERED 
(
	[game_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Player]    Script Date: 7/31/2018 4:22:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Player](
	[player_id] [int] NOT NULL,
	[first_name] [nvarchar](50) NULL,
	[last_name] [nvarchar](50) NULL,
	[address] [nvarchar](50) NULL,
	[postal_code] [nvarchar](6) NULL,
	[province] [nvarchar](2) NULL,
	[phone_number] [nvarchar](20) NULL,
 CONSTRAINT [PK_Player] PRIMARY KEY CLUSTERED 
(
	[player_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Player] SET (LOCK_ESCALATION = DISABLE)
GO
/****** Object:  Table [dbo].[PlayerAndGame]    Script Date: 7/31/2018 4:22:10 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PlayerAndGame](
	[player_game_id] [int] NOT NULL,
	[game_id] [int] NOT NULL,
	[player_id] [int] NOT NULL,
	[playing_date] [date] NULL,
	[score] [int] NULL,
 CONSTRAINT [PK_PlayerAndGame] PRIMARY KEY CLUSTERED 
(
	[player_game_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[Game] ([game_id], [game_title]) VALUES (1, N'Pac-Man')
INSERT [dbo].[Game] ([game_id], [game_title]) VALUES (2, N'Super Mario Bros. 2')
INSERT [dbo].[Player] ([player_id], [first_name], [last_name], [address], [postal_code], [province], [phone_number]) VALUES (1, N'Hua', N'Wang', N'1 Happy R', N'M2B3K5', N'ON', N'4165559999')
INSERT [dbo].[Player] ([player_id], [first_name], [last_name], [address], [postal_code], [province], [phone_number]) VALUES (2, N'May', N'Chen', N'2 Happy R', N'L3N5J7', N'BC', N'4379991033')
INSERT [dbo].[Player] ([player_id], [first_name], [last_name], [address], [postal_code], [province], [phone_number]) VALUES (3, N'William', N'Lin', N'3 Happy R', N'B2V4C6', N'AB', N'6148881111')
INSERT [dbo].[Player] ([player_id], [first_name], [last_name], [address], [postal_code], [province], [phone_number]) VALUES (4, N'Jack', N'Lo', N'4 Happy R', N'A4C4D5', N'BC', N'4162224444')
INSERT [dbo].[Player] ([player_id], [first_name], [last_name], [address], [postal_code], [province], [phone_number]) VALUES (5, N'Ed', N'White', N'5 Happy R', N'K4H2B6', N'BC', N'6448883333')
INSERT [dbo].[PlayerAndGame] ([player_game_id], [game_id], [player_id], [playing_date], [score]) VALUES (1, 2, 1, CAST(N'2018-07-31' AS Date), 10000)
INSERT [dbo].[PlayerAndGame] ([player_game_id], [game_id], [player_id], [playing_date], [score]) VALUES (2, 1, 2, CAST(N'2018-07-31' AS Date), 12000)
INSERT [dbo].[PlayerAndGame] ([player_game_id], [game_id], [player_id], [playing_date], [score]) VALUES (3, 2, 3, CAST(N'2018-07-31' AS Date), 5000)
INSERT [dbo].[PlayerAndGame] ([player_game_id], [game_id], [player_id], [playing_date], [score]) VALUES (4, 1, 4, CAST(N'2018-07-31' AS Date), 35000)
INSERT [dbo].[PlayerAndGame] ([player_game_id], [game_id], [player_id], [playing_date], [score]) VALUES (5, 1, 5, CAST(N'2018-07-31' AS Date), 2200)
ALTER TABLE [dbo].[PlayerAndGame]  WITH CHECK ADD  CONSTRAINT [FK_PlayerAndGame_Game] FOREIGN KEY([game_id])
REFERENCES [dbo].[Game] ([game_id])
GO
ALTER TABLE [dbo].[PlayerAndGame] CHECK CONSTRAINT [FK_PlayerAndGame_Game]
GO
ALTER TABLE [dbo].[PlayerAndGame]  WITH CHECK ADD  CONSTRAINT [FK_PlayerAndGame_Player] FOREIGN KEY([player_id])
REFERENCES [dbo].[Player] ([player_id])
GO
ALTER TABLE [dbo].[PlayerAndGame] CHECK CONSTRAINT [FK_PlayerAndGame_Player]
GO
USE [master]
GO
ALTER DATABASE [MyGame] SET  READ_WRITE 
GO
