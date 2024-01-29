CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome_completo` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `lojas_permitidas` varchar(255),
  `perfil` varchar(100),
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_usuario` (`email`)
);