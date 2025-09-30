-- Dados iniciais para o banco H2 (plano gratuito do Render)
-- Este arquivo é executado a cada startup da aplicação

-- Inserir dados de exemplo para Pátios
INSERT INTO Patios (id_patio, nome, telefone, endereco) VALUES 
(1, 'Pátio Central', '(11)98231-3429', 'Rua das Flores, 123'),
(2, 'Pátio Norte', '(11)98231-3429', 'Av. Norte, 456'),
(3, 'Pátio Sul', '(11)98231-3429', 'Rua Sul, 789');

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

-- Inserir dados de exemplo para Pontos de Leitura (áreas do pátio)
INSERT INTO Pontos_Leitura (id_ponto, id_patio, nome_ponto, descricao) VALUES 
(1, 1, 'defeito motor', 'Área de motos com defeito no motor'),
(2, 1, 'dano estrutural', 'Área de motos com dano estrutural'),
(3, 1, 'minha mottu', 'Área de motos da Mottu'),
(4, 1, 'agendamento', 'Área de motos aguardando agendamento'),
(5, 1, 'pendência', 'Área de motos com pendência'),
(6, 1, 'reparos simples', 'Área para reparos simples'),
(7, 1, 'para alugar', 'Área de motos disponíveis para aluguel'),
(8, 1, 'sem placa', 'Área de motos sem placa'),
(9, 2, 'defeito motor', 'Área de motos com defeito no motor'),
(10, 2, 'dano estrutural', 'Área de motos com dano estrutural'),
(11, 2, 'minha mottu', 'Área de motos da Mottu'),
(12, 2, 'agendamento', 'Área de motos aguardando agendamento'),
(13, 2, 'pendência', 'Área de motos com pendência'),
(14, 2, 'reparos simples', 'Área para reparos simples'),
(15, 2, 'para alugar', 'Área de motos disponíveis para aluguel'),
(16, 2, 'sem placa', 'Área de motos sem placa'),
(17, 3, 'defeito motor', 'Área de motos com defeito no motor'),
(18, 3, 'dano estrutural', 'Área de motos com dano estrutural'),
(19, 3, 'minha mottu', 'Área de motos da Mottu'),
(20, 3, 'agendamento', 'Área de motos aguardando agendamento'),
(21, 3, 'pendência', 'Área de motos com pendência'),
(22, 3, 'reparos simples', 'Área para reparos simples'),
(23, 3, 'para alugar', 'Área de motos disponíveis para aluguel'),
(24, 3, 'sem placa', 'Área de motos sem placa');

-- Inserir dados de exemplo para Movimentações (motos distribuídas pelos pontos)
INSERT INTO Movimentacoes (id_movimentacao, id_moto, id_ponto, data_hora) VALUES 
(1, 'MOTO001', 5, '2024-01-15 08:30:00'),  -- MOTO001 no ponto "pendência" do Pátio Central
(2, 'MOTO002', 6, '2024-01-15 09:15:00'),  -- MOTO002 no ponto "reparos simples" do Pátio Central
(3, 'MOTO003', 5, '2024-01-15 10:00:00'),  -- MOTO003 no ponto "pendência" do Pátio Central
(4, 'MOTO004', 5, '2024-01-15 11:30:00'),  -- MOTO004 no ponto "pendência" do Pátio Central
(5, 'MOTO005', 5, '2024-01-15 12:00:00'),  -- MOTO005 no ponto "pendência" do Pátio Central
(6, 'MOTO006', 15, '2024-01-15 13:00:00'), -- MOTO006 no ponto "para alugar" do Pátio Norte
(7, 'MOTO007', 11, '2024-01-15 14:00:00'), -- MOTO007 no ponto "minha mottu" do Pátio Norte
(8, 'MOTO008', 16, '2024-01-15 15:00:00'), -- MOTO008 no ponto "sem placa" do Pátio Norte
(9, 'MOTO009', 17, '2024-01-15 16:00:00'); -- MOTO009 no ponto "defeito motor" do Pátio Sul
