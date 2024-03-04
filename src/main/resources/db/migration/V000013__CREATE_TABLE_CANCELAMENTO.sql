CREATE TABLE `cancelamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome_comerciante` varchar(255) NOT NULL,
  `comerciante_id` varchar(255) NOT NULL,
  `periodo_id` varchar(255) NOT NULL,
  `pedido_id` varchar(255) NOT NULL,
  `valor` decimal(19,2) default 0.0,
  PRIMARY KEY (`id`)
);
