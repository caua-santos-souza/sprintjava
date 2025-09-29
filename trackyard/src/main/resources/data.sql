-- Dados iniciais para o banco H2 (plano gratuito do Render)
-- Este arquivo é executado a cada startup da aplicação

-- Inserir dados de exemplo para Pátios
INSERT INTO patios (id, nome, capacidade, endereco) VALUES 
(1, 'Pátio Central', 100, 'Rua das Flores, 123'),
(2, 'Pátio Norte', 50, 'Av. Norte, 456'),
(3, 'Pátio Sul', 75, 'Rua Sul, 789')
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- Inserir dados de exemplo para Pontos de Leitura
INSERT INTO pontos_leitura (id, nome, localizacao, patio_id) VALUES 
(1, 'Entrada Principal', 'Portão 1', 1),
(2, 'Entrada Secundária', 'Portão 2', 1),
(3, 'Saída Norte', 'Portão 1', 2),
(4, 'Entrada Sul', 'Portão 1', 3)
ON DUPLICATE KEY UPDATE nome = VALUES(nome);

-- Inserir dados de exemplo para Motos
INSERT INTO motos (id, placa, modelo, cor, ano, status) VALUES 
(1, 'ABC-1234', 'Honda CG 160', 'Vermelha', 2023, 'DISPONIVEL'),
(2, 'DEF-5678', 'Yamaha Fazer 250', 'Azul', 2022, 'DISPONIVEL'),
(3, 'GHI-9012', 'Honda Biz 125', 'Preta', 2023, 'EM_USO'),
(4, 'JKL-3456', 'Yamaha Factor 150', 'Branca', 2022, 'DISPONIVEL')
ON DUPLICATE KEY UPDATE placa = VALUES(placa);

-- Inserir dados de exemplo para Movimentações
INSERT INTO movimentacoes (id, moto_id, patio_origem_id, patio_destino_id, ponto_leitura_id, data_movimentacao, tipo_movimentacao, observacoes) VALUES 
(1, 1, NULL, 1, 1, '2024-01-15 08:30:00', 'ENTRADA', 'Moto chegou ao pátio'),
(2, 2, NULL, 1, 2, '2024-01-15 09:15:00', 'ENTRADA', 'Moto chegou ao pátio'),
(3, 3, 1, 2, 3, '2024-01-15 10:00:00', 'TRANSFERENCIA', 'Transferência entre pátios'),
(4, 4, NULL, 3, 4, '2024-01-15 11:30:00', 'ENTRADA', 'Moto chegou ao pátio')
ON DUPLICATE KEY UPDATE data_movimentacao = VALUES(data_movimentacao);
