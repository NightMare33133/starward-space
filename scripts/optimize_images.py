import os
from PIL import Image

def optimize_images():
    base_dir = os.path.abspath('starward-frontend/public/images')
    print(f"Scanning {base_dir} for optimization...")
    
    total_before = 0
    total_after_webp = 0
    total_after_compat = 0
    
    for root, dirs, files in os.walk(base_dir):
        for f in sorted(files):
            if f.endswith(('.png', '.jpg', '.jpeg')) and not f.endswith('.test.webp'):
                src_path = os.path.join(root, f)
                orig_size = os.path.getsize(src_path)
                total_before += orig_size
                
                im = Image.open(src_path)
                w, h = im.size
                
                # Intelligent downsampling rules
                if '_avatar' in f:
                    # Avatars only need 360x360
                    target_w, target_h = (360, 360)
                    im_resized = im.resize((target_w, target_h), Image.Resampling.LANCZOS)
                elif f == 'pompom.png':
                    # Pom-Pom mascot used in nav/modals <= 128px
                    target_w, target_h = (320, 320)
                    im_resized = im.resize((target_w, target_h), Image.Resampling.LANCZOS)
                elif w > 1920:
                    # Large wallpapers downscaled to standard 1080p
                    scale = 1920 / w
                    im_resized = im.resize((1920, int(h * scale)), Image.Resampling.LANCZOS)
                elif f.endswith('.png') and w == 902:
                    # Light cone character cards downscaled to 800w
                    scale = 800 / w
                    im_resized = im.resize((800, int(h * scale)), Image.Resampling.LANCZOS)
                else:
                    im_resized = im
                
                # 1. Generate modern WebP asset
                webp_path = os.path.splitext(src_path)[0] + '.webp'
                im_resized.save(webp_path, 'WEBP', quality=88, method=6)
                webp_size = os.path.getsize(webp_path)
                total_after_webp += webp_size
                
                # 2. Also re-compress the original format in-place for backward compatibility
                if f.endswith('.png'):
                    im_resized.save(src_path, 'PNG', optimize=True)
                elif f.endswith(('.jpg', '.jpeg')):
                    im_resized.save(src_path, 'JPEG', quality=88, optimize=True)
                compat_size = os.path.getsize(src_path)
                total_after_compat += compat_size
                
                print(f"✓ {f:25}: {orig_size/1024:7.1f} KB -> WebP: {webp_size/1024:5.1f} KB | In-place: {compat_size/1024:5.1f} KB")

    print("\n" + "="*50)
    print(f"Original Total:  {total_before/1024/1024:.2f} MB")
    print(f"WebP Total:      {total_after_webp/1024/1024:.2f} MB (Saved {100 - (total_after_webp/total_before*100):.1f}%)")
    print(f"In-place Total:  {total_after_compat/1024/1024:.2f} MB (Saved {100 - (total_after_compat/total_before*100):.1f}%)")
    print("="*50)

if __name__ == '__main__':
    optimize_images()
