IF DB_ID(N'ClothingShop') IS NULL
BEGIN
    CREATE DATABASE ClothingShop;
END;
GO

USE ClothingShop;
GO

IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
BEGIN
CREATE TABLE dbo.Users
(
    id INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) UNIQUE NOT NULL,
    username NVARCHAR(100) UNIQUE NOT NULL,
    fullname NVARCHAR(255),
    password NVARCHAR(255) NOT NULL,
    avatar NVARCHAR(255),
    roleid INT NOT NULL CONSTRAINT DF_Users_RoleId DEFAULT 5,
    phone NVARCHAR(20),
    status INT NOT NULL CONSTRAINT DF_Users_Status DEFAULT 0,
    otp VARCHAR(10),
    createdDate DATETIME NOT NULL CONSTRAINT DF_Users_CreatedDate DEFAULT GETDATE(),
    CONSTRAINT CK_Users_Status CHECK (status IN (0, 1))
);
END;
GO

IF OBJECT_ID(N'dbo.Category', N'U') IS NULL
BEGIN
CREATE TABLE dbo.Category
(
    cate_id INT IDENTITY(1,1) PRIMARY KEY,
    cate_name NVARCHAR(255) NOT NULL,
    icons NVARCHAR(255)
);
END;
GO

IF OBJECT_ID(N'dbo.Products', N'U') IS NULL
BEGIN
CREATE TABLE dbo.Products
(
    product_id INT IDENTITY(1,1) PRIMARY KEY,
    product_name NVARCHAR(255) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    description NVARCHAR(MAX),
    image NVARCHAR(255),
    createdDate DATETIME NOT NULL CONSTRAINT DF_Products_CreatedDate DEFAULT GETDATE(),
    cate_id INT NOT NULL,
    CONSTRAINT CK_Products_Price CHECK (price > 0),
    CONSTRAINT FK_Product_Category FOREIGN KEY(cate_id) REFERENCES dbo.Category(cate_id)
);
END;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Quần áo nam')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Quần áo nam', 'ao-nam.png');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Quần áo nữ')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Quần áo nữ', 'ao-nu.png');
IF NOT EXISTS (SELECT 1 FROM dbo.Category WHERE cate_name = N'Giày dép')
    INSERT INTO dbo.Category(cate_name, icons) VALUES (N'Giày dép', 'giay-dep.png');
GO

-- Tài khoản mẫu. Password legacy sẽ tự được migrate sang PBKDF2 sau lần đăng nhập thành công đầu tiên.
IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE username = 'admin')
BEGIN
INSERT INTO dbo.Users(email, username, fullname, password, roleid, status)
VALUES ('admin@gmail.com', 'admin', N'Administrator', '123456', 1, 1);
END;
GO

SELECT * FROM dbo.Users;
SELECT * FROM dbo.Category;
SELECT * FROM dbo.Products;
USE ClothingShop;
GO

UPDATE Category
SET icons = 'ao-nam.jpg'
WHERE cate_name = N'Quần áo nam';

UPDATE Category
SET icons = 'ao-nu.jpg'
WHERE cate_name = N'Quần áo nữ';

UPDATE Category
SET icons = 'giay-dep.jpg'
WHERE cate_name = N'Giày dép';
GO

SELECT * FROM Category;
USE ClothingShop;
GO

/* =========================
   1. ĐẢM BẢO CATEGORY TỒN TẠI
   ========================= */

IF NOT EXISTS (
    SELECT 1 FROM Category
    WHERE cate_name = N'Quần áo nam'
)
BEGIN
INSERT INTO Category(cate_name, icons)
VALUES (N'Quần áo nam', 'ao-nam.jpg');
END;

IF NOT EXISTS (
    SELECT 1 FROM Category
    WHERE cate_name = N'Quần áo nữ'
)
BEGIN
INSERT INTO Category(cate_name, icons)
VALUES (N'Quần áo nữ', 'ao-nu.jpg');
END;

IF NOT EXISTS (
    SELECT 1 FROM Category
    WHERE cate_name = N'Giày dép'
)
BEGIN
INSERT INTO Category(cate_name, icons)
VALUES (N'Giày dép', 'giay-dep.jpg');
END;
GO


/* =========================
   2. LẤY ID CATEGORY
   ========================= */

DECLARE @CateNam INT;
DECLARE @CateNu INT;

SELECT @CateNam = cate_id
FROM Category
WHERE cate_name = N'Quần áo nam';

SELECT @CateNu = cate_id
FROM Category
WHERE cate_name = N'Quần áo nữ';


/* =========================
   3. THÊM 14 SẢN PHẨM TEST
   ========================= */

IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo thun nam basic'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo thun nam basic',
    199000,
    N'Áo thun nam basic, dễ phối đồ.',
    'ao-nam.jpg',
    DATEADD(MINUTE, -13, GETDATE()),
    @CateNam
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo sơ mi nam'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo sơ mi nam',
    299000,
    N'Áo sơ mi nam phong cách thanh lịch.',
    'ao-nam.jpg',
    DATEADD(MINUTE, -12, GETDATE()),
    @CateNam
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo polo nam'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo polo nam',
    259000,
    N'Áo polo nam trẻ trung.',
    'ao-nam.jpg',
    DATEADD(MINUTE, -11, GETDATE()),
    @CateNam
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo khoác nam'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo khoác nam',
    449000,
    N'Áo khoác nam thời trang.',
    'ao-nam.jpg',
    DATEADD(MINUTE, -10, GETDATE()),
    @CateNam
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo hoodie nam'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo hoodie nam',
    399000,
    N'Áo hoodie nam form rộng.',
    'ao-nam.jpg',
    DATEADD(MINUTE, -9, GETDATE()),
    @CateNam
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo sweater nam'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo sweater nam',
    349000,
    N'Áo sweater nam phong cách năng động.',
    'ao-nam.jpg',
    DATEADD(MINUTE, -8, GETDATE()),
    @CateNam
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo thun nữ basic'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo thun nữ basic',
    189000,
    N'Áo thun nữ basic.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -7, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo sơ mi nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo sơ mi nữ',
    289000,
    N'Áo sơ mi nữ công sở.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -6, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo kiểu nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo kiểu nữ',
    279000,
    N'Áo kiểu nữ thanh lịch.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -5, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo cardigan nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo cardigan nữ',
    359000,
    N'Áo cardigan nữ nhẹ nhàng.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -4, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo croptop nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo croptop nữ',
    219000,
    N'Áo croptop nữ trẻ trung.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -3, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo blazer nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo blazer nữ',
    499000,
    N'Áo blazer nữ thanh lịch.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -2, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo hoodie nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo hoodie nữ',
    389000,
    N'Áo hoodie nữ form rộng.',
    'ao-nu.jpg',
    DATEADD(MINUTE, -1, GETDATE()),
    @CateNu
);


IF NOT EXISTS (
    SELECT 1 FROM Products
    WHERE product_name = N'Áo len nữ'
)
INSERT INTO Products
(
    product_name,
    price,
    description,
    image,
    createdDate,
    cate_id
)
VALUES
(
    N'Áo len nữ',
    329000,
    N'Áo len nữ sản phẩm mới nhất.',
    'ao-nu.jpg',
    GETDATE(),
    @CateNu
);

GO
SELECT
    p.product_id,
    p.product_name,
    p.price,
    p.image,
    c.cate_name,
    p.createdDate
FROM Products p
         JOIN Category c
              ON p.cate_id = c.cate_id
ORDER BY p.createdDate DESC;