-- Dados iniciais para o banco H2 (plano gratuito do Render)
-- Este arquivo é executado a cada startup da aplicação

-- Inserir dados de exemplo para Pátios
INSERT INTO Patios (id_patio, nome, telefone, endereco) 
SELECT 1, 'Pátio Central', '(11)98231-3429', 'Rua das Flores, 123' WHERE NOT EXISTS (SELECT 1 FROM Patios WHERE id_patio = 1);
INSERT INTO Patios (id_patio, nome, telefone, endereco) 
SELECT 2, 'Pátio Norte', '(11)98231-3429', 'Av. Norte, 456' WHERE NOT EXISTS (SELECT 1 FROM Patios WHERE id_patio = 2);
INSERT INTO Patios (id_patio, nome, telefone, endereco) 
SELECT 3, 'Pátio Sul', '(11)98231-3429', 'Rua Sul, 789' WHERE NOT EXISTS (SELECT 1 FROM Patios WHERE id_patio = 3);

-- Inserir dados de exemplo para Motos (apenas modelos Pop, Sport, E)
-- Só insere se não existir
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO001', 'ABC-1234', 'Pop' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO001');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO002', 'DEF-5678', 'Sport' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO002');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO003', 'GHI-9012', 'E' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO003');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO004', 'JKL-3456', 'Pop' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO004');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO005', 'MNO-3344', 'Sport' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO005');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO006', 'PQR-5566', 'E' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO006');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO007', 'STU-7788', 'Pop' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO007');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO008', 'VWX-9999', 'Sport' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO008');
INSERT INTO Motos (id_moto, placa, modelo) 
SELECT 'MOTO009', 'YZA-1011', 'E' WHERE NOT EXISTS (SELECT 1 FROM Motos WHERE id_moto = 'MOTO009');

-- Inserir dados de exemplo para Pontos de Leitura (apenas no Pátio Central)
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 1, 1, 'defeito motor', 'Área de motos com defeito no motor' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 1);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 2, 1, 'dano estrutural', 'Área de motos com dano estrutural' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 2);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 3, 1, 'minha mottu', 'Área de motos da Mottu' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 3);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 4, 1, 'agendamento', 'Área de motos aguardando agendamento' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 4);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 5, 1, 'pendência', 'Área de motos com pendência' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 5);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 6, 1, 'reparos simples', 'Área para reparos simples' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 6);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 7, 1, 'para alugar', 'Área de motos disponíveis para aluguel' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 7);
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) 
SELECT 8, 1, 'sem placa', 'Área de motos sem placa' WHERE NOT EXISTS (SELECT 1 FROM Pontos_Leitura WHERE id_ponto = 8);

-- Inserir dados de exemplo para Movimentações (motos distribuídas pelos pontos)
-- Só insere se não existir (sem IDs específicos para evitar conflitos)
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO001', 5, '2024-01-15 08:30:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO001' AND id_ponto = 5);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO002', 6, '2024-01-15 09:15:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO002' AND id_ponto = 6);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO003', 5, '2024-01-15 10:00:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO003' AND id_ponto = 5);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO004', 5, '2024-01-15 11:30:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO004' AND id_ponto = 5);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO005', 5, '2024-01-15 12:00:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO005' AND id_ponto = 5);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO006', 7, '2024-01-15 13:00:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO006' AND id_ponto = 7);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO007', 3, '2024-01-15 14:00:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO007' AND id_ponto = 3);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO008', 8, '2024-01-15 15:00:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO008' AND id_ponto = 8);
INSERT INTO Movimentacoes (id_moto, id_ponto, data_hora) 
SELECT 'MOTO009', 1, '2024-01-15 16:00:00' WHERE NOT EXISTS (SELECT 1 FROM Movimentacoes WHERE id_moto = 'MOTO009' AND id_ponto = 1);
