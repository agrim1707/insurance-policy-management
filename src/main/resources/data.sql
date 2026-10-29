-- Seed data: policy plans available for browsing and purchase.
INSERT INTO policies (name, category, description, premium_amount, coverage_amount, tenure_months, active)
VALUES
 ('Aegis Shield Life Cover',  'LIFE',    'Comprehensive life insurance cover with natural death and accidental death benefit.', 12500.00, 5000000.00, 12,   TRUE),
 ('Secure Tomorrow Plus',     'LIFE',    'Family term plan with premium waiver and child education rider included.',            18500.00, 7500000.00, 120,  TRUE),
 ('MediCare Health Plan',     'HEALTH',  'Cashless hospitalisation cover for you and your family at 4000+ network hospitals.',   8900.00,  500000.00, 12,   TRUE),
 ('Wellness Maxi Health',     'HEALTH',  'Critical illness cover with annual free health check-up and daily cash allowance.',   15200.00, 1000000.00, 24,   TRUE),
 ('DriveSafe Motor Plan',     'VEHICLE', 'Third-party plus own damage cover for private cars with roadside assistance.',          6400.00,  250000.00, 12,   TRUE),
 ('TravelSafe Global',        'TRAVEL',  'International travel insurance covering baggage loss, delay and medical emergencies.', 3200.00,  300000.00, 24,   TRUE),
 ('HomeGuard Property Cover', 'HOME',    'Fire, theft and natural calamity cover for your home and contents.',                   5100.00,  1500000.00, 36, TRUE);
