INSERT INTO public.goods (id, name, price)
VALUES ('123e4567-e89b-12d3-a456-426652340000', 'Product A', 100.50)
ON CONFLICT (id) DO UPDATE
SET
    name = EXCLUDED.name,
    price = EXCLUDED.price;