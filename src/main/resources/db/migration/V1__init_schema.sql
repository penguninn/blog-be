CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE EXTENSION IF NOT EXISTS citext;

CREATE TYPE USER_ROLE AS ENUM (
    'user',
    'admin'
);

CREATE TYPE POST_STATUS AS ENUM (
    'draft',
    'published'
);

CREATE TABLE IF NOT EXISTS public.users (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email CITEXT UNIQUE NOT NULL,
    erole USER_ROLE DEFAULT 'user' NOT NULL,
    password_hash TEXT NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    avatar_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.categories (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    display_name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.tags (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    display_name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.posts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    author_id UUID NOT NULL,
    category_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    post_content TEXT NOT NULL,
    thumbnail_url TEXT,
    status POST_STATUS DEFAULT 'draft' NOT NULL,
    view_count BIGINT DEFAULT 0 NOT NULL,
    search_vector TSVECTOR,
    published_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT post_author_fk FOREIGN KEY (
        author_id
    ) REFERENCES public.users (id) ON DELETE RESTRICT,
    CONSTRAINT post_category_fk FOREIGN KEY (
        category_id
    ) REFERENCES public.categories (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS public.comments (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    post_id UUID NOT NULL,
    author_id UUID NOT NULL,
    parent_id UUID,
    depth_count INT DEFAULT 0 NOT NULL,
    reply_count INT DEFAULT 0 NOT NULL,
    comment_content TEXT NOT NULL,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT comment_post_fk FOREIGN KEY (
        post_id
    ) REFERENCES public.posts (id),
    CONSTRAINT comment_author_fk FOREIGN KEY (
        author_id
    ) REFERENCES users (id),
    CONSTRAINT comment_pcomment_fk FOREIGN KEY (
        parent_id
    ) REFERENCES comments (id)
);

CREATE TABLE IF NOT EXISTS public.post_tags (
    post_id UUID NOT NULL,
    tag_id UUID NOT NULL,

    CONSTRAINT post_tag_post_fk FOREIGN KEY (
        post_id
    ) REFERENCES public.posts (id),
    CONSTRAINT post_tag_tag_fk FOREIGN KEY (
        tag_id
    ) REFERENCES public.tags (id)
);
