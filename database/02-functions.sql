CREATE OR REPLACE FUNCTION create_default_categories()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO categories (title, description, color, icon, user_id)
    VALUES
        ('Alimentação', 'Gastos com mercado e restaurantes', 'orange', 'utensils', NEW.id),
        ('Transporte', 'Combustível, Uber e manutenção', 'blue', 'carfront', NEW.id),
        ('Saúde', 'Farmácia e consultas', 'red', 'heartpulse', NEW.id),
        ('Lazer', 'Cinema, shows e viagens', 'purple', 'ticket', NEW.id),
        ('Moradia', 'Aluguel, conta de luz e água', 'yellow', 'house', NEW.id),
        ('Educação', 'Cursos e livros', 'green', 'bookopen', NEW.id),
        ('Compras', 'Roupas e eletrônicos', 'pink', 'shoppingcart', NEW.id),
        ('Investimentos', 'Reserva de emergência e aportes', 'gray', 'piggybank', NEW.id);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;