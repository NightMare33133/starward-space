export interface Result<T> {
  code: number;
  message: string;
  data: T;
  timestamp: number;
}

export interface Tag {
  id: number;
  name: string;
  slug: string;
  color?: string;
}

export interface PostListVO {
  id: number;
  title: string;
  slug: string;
  summary: string;
  coverImage?: string;
  status?: 'PUBLISHED' | 'DRAFT';
  viewCount: number;
  isPinned: boolean | number;
  createdAt: string;
  updatedAt?: string;
  publishedAt?: string;
  tags: Tag[];
}

export interface PostDetailVO {
  id: number;
  title: string;
  slug: string;
  summary: string;
  content?: string;
  contentMd?: string;
  coverImage?: string;
  status?: 'PUBLISHED' | 'DRAFT';
  viewCount: number;
  isPinned: boolean | number;
  createdAt: string;
  updatedAt?: string;
  publishedAt?: string;
  tags: Tag[];
}

export interface PostCreateRequest {
  title: string;
  slug: string;
  summary: string;
  contentMd: string;
  coverImage?: string;
  status: 'PUBLISHED' | 'DRAFT';
  isPinned: number | boolean;
  tagIds: number[];
}

export interface PostUpdateRequest {
  title: string;
  slug: string;
  summary: string;
  contentMd: string;
  coverImage?: string;
  status: 'PUBLISHED' | 'DRAFT';
  isPinned: number | boolean;
  tagIds: number[];
}

export interface TagCreateRequest {
  name: string;
  slug: string;
  color: string;
}

export interface Moment {
  id: number;
  content: string;
  mood?: string;
  moodEmoji?: string;
  location?: string;
  likeCount?: number;
  createdAt: string;
}

export interface MomentCreateRequest {
  content: string;
  mood?: string;
  imagesJson?: string;
}

export interface Song {
  id: string;
  title: string;
  artist: string;
  cover: string;
  url: string;
  duration?: number;
  lyrics?: string;
  theme?: string;
}

export interface Photo {
  id: string;
  title: string;
  location: string;
  date: string;
  url: string;
  category: string;
  params?: string; // e.g. "Sony A7M4 · 35mm f/1.4"
  rarity?: number; // 4 or 5 for light cones
}
