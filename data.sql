USE ShopApp;

INSERT INTO `category` (`id`, `name_category`) VALUES
(1, 'Dell'),
(2, 'Asus'),
(3, 'Accer'),
(4, 'HP'),
(5, 'Lenovo'),
(6, 'Macbook');

INSERT INTO `product` (`id`, `name_product`,`id_category`, `price`, `thumbnail`, `description`, `create_at`, `update_at`) VALUES
(1,'Laptop Dell Inspiron 15 3520',1,14690000,' ','i5 1235U/8GB/256GB/120Hz/OfficeHS/Win11','2023-07-31 08:28:28', '2023-07-31 08:28:28'),
(2,'Dell Latitude 3440',1,29800000,' ','i7-1355U - 16GB - 512GB - Win 11 Pro','2023-07-31 08:28:28','2023-11-10 09:27:31'),
(3,'Dell Latitude 7340 XCTO',1,38690000,' ','i5-1335U - 16GB - 512GB - Intel Iris Xe - 13.3 inch FHD','2023-07-31 08:28:28','2023-07-31 08:28:28'),
(4,'Dell Gaming G15 5535 Ryzen 5',1,20700000,' ','7640HS/RAM 16GB/SSD 1TB/, RTX 3050 6G/ 15.6','2023-07-31 08:28:28','2023-07-31 08:28:28'),
(5,'Dell Inspiron 14 7430',1,15900000,' ','Core i5-1335U Ram 8GB SSD 512GB 14','2023-07-31 08:28:28','2023-07-31 08:28:28');

