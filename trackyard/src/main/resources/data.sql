-- Dados iniciais para o banco H2 (plano gratuito do Render)
-- Este arquivo é executado a cada startup da aplicação

-- Inserir dados de exemplo para Pátios
INSERT INTO Patios (id_patio, nome, endereco) VALUES 
(1, 'Pátio Central', 'Rua das Flores, 123'),
(2, 'Pátio Norte', 'Av. Norte, 456'),
(3, 'Pátio Sul', 'Rua Sul, 789');

-- Inserir dados de exemplo para Motos
INSERT INTO Motos (id_moto, placa, modelo) VALUES 
('MOTO001', 'ABC-1234', 'Honda CG 160'),
('MOTO002', 'DEF-5678', 'Yamaha Fazer 250'),
('MOTO003', 'GHI-9012', 'Honda Biz 125'),
('MOTO004', 'JKL-3456', 'Yamaha Factor 150');
