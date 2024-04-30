insert into infs7202.user (id, role_id, restaurant_id, email, password, is_active, created_at, updated_at)
values  (1, 1, null, 'admin@example.com', '$2y$10$Y9AcCSJmCctbbKIQx3ijV.g0bp0opJuocjmcbLxeitEXI.UVpm.Cm', 1, '2024-04-28 19:17:55', '2024-04-28 19:17:55'),
        (2, 2, 1, 'owner@example.com', '$2a$10$/d.vWGH8VVpSaM9S7OJQV.qZQjwGBwhQZf/2PvcCABJpW6vBCgu/6', 1, '2024-04-28 01:13:37', '2024-04-28 19:23:35'),
        (3, 2, 2, 'owner2@example.com', '$2a$10$MKObXeZ4UINDJxNZxswcPe3pJwAc0JEc0J47r4Sgoma0q.igBeZry', 1, '2024-04-28 01:16:09', '2024-04-28 01:16:09'),
        (4, 2, 3, 'owner3@example.com', '$2a$10$xFlnhYF0NhbHnPIvfwJ3nuq1uNUUNkRltvHH8T7AqOqFRrofy5d3i', 1, '2024-04-28 01:18:01', '2024-04-28 01:18:01'),
        (5, 1, null, 'admin2@example.com', '$2a$10$/p9SJgrkBEwFkyog4pttyuGml.8AF7rZ0OxIReubkqBChcyh1AW7i', 1, '2024-04-28 17:07:58', '2024-04-28 18:21:44'),
        (6, 1, null, 'admin3@example.com', '$2a$10$wjmKLKm7p.ooqpkKeip9Me3uGT4IxwFSLlbQcIxekdkAo3fKmj95C', 1, '2024-04-28 17:22:32', '2024-04-28 17:22:32'),
        (7, 1, null, 'admin4@example.com', '$2a$10$RPnQAR7bkj1jyN8J7z6EbOmM7ctuYNvAqmT2w75I5eDNvrgqr26pe', 0, '2024-04-28 17:22:57', '2024-04-28 17:22:57'),
        (8, 1, null, 'admin5@example.com', '$2a$10$pMFMpsawRzBIwlGn8Jzo9uyUbXoRpHI0fFTqpOHRxkWDhHEGQLtq2', 1, '2024-04-28 17:26:34', '2024-04-28 17:26:34'),
        (9, 1, null, 'admin6@example.com', '$2a$10$FKksM7p1WJM5cYl9U3xdseMhjDGEevnUv/VCuQIgQvCdG2QWv2nvO', 1, '2024-04-28 17:28:08', '2024-04-28 17:28:08'),
        (10, 2, 4, 'owner10@example.com', '$2a$10$y3gmiCAMtvftsLuFOfcV0OJ320J.37RpruYWOqFKnIJ.25SgHPcXe', 1, '2024-04-28 17:31:29', '2024-04-28 17:31:29');