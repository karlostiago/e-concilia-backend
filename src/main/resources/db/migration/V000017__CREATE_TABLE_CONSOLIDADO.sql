CREATE TABLE `consolidado` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`forma_pagamento` VARCHAR(255) NOT NULL,
	`periodo` DATE NOT NULL,
	`quantidade_venda` NUMERIC(19,2) NOT NULL,
	`ticket_medio` NUMERIC(10,2) NOT NULL,
	`total_bruto` NUMERIC(10,2) NOT NULL,
	`total_cancelado` NUMERIC(10,2) NOT NULL,
	`total_comissao` NUMERIC(10,2) NOT NULL,
	`total_liquido` NUMERIC(10,2) NOT NULL,
	`total_promocao` NUMERIC(10,2) NOT NULL,
	`total_recebido` NUMERIC(10,2) NOT NULL,
	`total_taxa_entrega` NUMERIC(10,2) NOT NULL,
	`total_transacao_pagamento` NUMERIC(10,2) NOT NULL,
	`empresa_id` bigint NOT NULL,
	`operadora_id` bigint NOT NULL,
	PRIMARY KEY (`id`)
);