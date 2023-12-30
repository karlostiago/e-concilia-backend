CREATE TABLE `operadora` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativo` tinyint(1) NOT NULL DEFAULT '0',
  `descricao` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_descricao` (`descricao`)
);