CREATE TABLE `parametro` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo_parametro` varchar(100) NOT NULL,
  `ativo` bool DEFAULT FALSE,
  PRIMARY KEY (`id`)
);