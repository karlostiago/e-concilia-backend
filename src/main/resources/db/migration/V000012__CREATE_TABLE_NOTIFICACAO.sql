CREATE TABLE `notificacao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mensagem` TEXT NOT NULL,
  `lida` tinyint(1) NOT NULL DEFAULT '0',
  `resolvida` tinyint(1) NOT NULL DEFAULT '0',
  `empresa_id` bigint NOT NULL,
  `tipo` INT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notificacao_empresa_id` (`empresa_id`),
  CONSTRAINT `fk_notificacao_empresa_id` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`)
);