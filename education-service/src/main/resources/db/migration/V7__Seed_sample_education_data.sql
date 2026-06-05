INSERT INTO university (id, uuid, name, slug)
VALUES
    (1, '11111111-1111-1111-1111-111111111111', 'Dai hoc Bach khoa Ha Noi', 'dai-hoc-bach-khoa-ha-noi'),
    (2, '22222222-2222-2222-2222-222222222222', 'Dai hoc Quoc gia Ha Noi', 'dai-hoc-quoc-gia-ha-noi')
ON CONFLICT (id) DO NOTHING;

INSERT INTO faculty (id, uuid, university_id, name, slug)
VALUES
    (1, '33333333-3333-3333-3333-333333333333', 1, 'Cong nghe thong tin', 'cong-nghe-thong-tin'),
    (2, '44444444-4444-4444-4444-444444444444', 2, 'Kinh te', 'kinh-te')
ON CONFLICT (id) DO NOTHING;

INSERT INTO major (id, uuid, faculty_id, name, slug)
VALUES
    (1, '55555555-5555-5555-5555-555555555555', 1, 'Khoa hoc may tinh', 'khoa-hoc-may-tinh'),
    (2, '66666666-6666-6666-6666-666666666666', 2, 'Quan tri kinh doanh', 'quan-tri-kinh-doanh')
ON CONFLICT (id) DO NOTHING;

INSERT INTO subject (id, uuid, major_id, name, slug)
VALUES
    (1, '77777777-7777-7777-7777-777777777777', 1, 'Co so du lieu', 'co-so-du-lieu'),
    (2, '88888888-8888-8888-8888-888888888888', 1, 'Lap trinh Java', 'lap-trinh-java'),
    (3, '99999999-9999-9999-9999-999999999999', 2, 'Marketing can ban', 'marketing-can-ban')
ON CONFLICT (id) DO NOTHING;

INSERT INTO document (
    id,
    uuid,
    university_id,
    subject_id,
    media_id,
    thumbnail_url,
    title,
    year,
    page_count,
    like_count,
    comment_count,
    description,
    status
)
VALUES
    (
        1,
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        1,
        1,
        NULL,
        'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800&h=600&fit=crop',
        'De cuong Co so du lieu 2025',
        2025,
        19,
        15,
        3,
        'Tai lieu mau de test danh sach tren frontend.',
        'READY'
    ),
    (
        2,
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        1,
        2,
        NULL,
        'https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=800&h=600&fit=crop',
        'Bai tap Lap trinh Java',
        2024,
        28,
        21,
        5,
        'Tai lieu mau de test danh sach tren frontend.',
        'READY'
    ),
    (
        3,
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        2,
        3,
        NULL,
        'https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=800&h=600&fit=crop',
        'Slide Marketing can ban',
        2025,
        42,
        8,
        1,
        'Tai lieu mau cho khoi nganh kinh te.',
        'READY'
    ),
    (
        4,
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        1,
        1,
        NULL,
        'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=800&h=600&fit=crop',
        'Bao cao do an He quan tri co so du lieu',
        2024,
        36,
        12,
        4,
        'Tai lieu mau co du thumbnail, so trang va thong ke.',
        'READY'
    ),
    (
        5,
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        1,
        2,
        NULL,
        'https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=800&h=600&fit=crop',
        'Giao trinh Lap trinh Java nang cao',
        2023,
        64,
        31,
        7,
        'Tai lieu mau phuc vu test phan trang.',
        'READY'
    ),
    (
        6,
        'ffffffff-ffff-ffff-ffff-ffffffffffff',
        2,
        3,
        NULL,
        'https://images.unsplash.com/photo-1557804506-669a67965ba0?w=800&h=600&fit=crop',
        'Tieu luan Marketing chien luoc',
        2024,
        22,
        9,
        2,
        'Tai lieu mau cho nganh kinh te.',
        'READY'
    ),
    (
        7,
        '12345678-1234-1234-1234-123456789abc',
        1,
        1,
        NULL,
        'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=800&h=600&fit=crop',
        'Tong hop cau hoi on tap Co so du lieu',
        2025,
        18,
        17,
        6,
        'Tai lieu mau nam o trang thu hai khi pageSize bang 5.',
        'READY'
    )
ON CONFLICT (id) DO NOTHING;

SELECT setval('university_id_seq', (SELECT MAX(id) FROM university));
SELECT setval('faculty_id_seq', (SELECT MAX(id) FROM faculty));
SELECT setval('major_id_seq', (SELECT MAX(id) FROM major));
SELECT setval('subject_id_seq', (SELECT MAX(id) FROM subject));
SELECT setval('document_id_seq', (SELECT MAX(id) FROM document));
