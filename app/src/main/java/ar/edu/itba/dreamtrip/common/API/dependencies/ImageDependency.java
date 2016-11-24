package ar.edu.itba.dreamtrip.common.API.dependencies;

import ar.edu.itba.dreamtrip.common.API.ImageType;

public class ImageDependency extends Dependency {

    private String url;

    public ImageType getImageType() {
        return imageType;
    }

    private ImageType imageType;

    public String getUrl() {
        return url;
    }

    public ImageDependency(String url, ImageType imageType) {
        super(DependencyType.IMAGE);
        this.url = url;
        this.imageType = imageType;
    }

//    public ImageDependency(String url, ImageType imageType,HashSet<Dependency> dependencies) {
//        super(DependencyType.IMAGE, dependencies);
//        this.url = url;
//        this.imageType = imageType;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ImageDependency that = (ImageDependency) o;

        return url.equals(that.url);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
