package com.blueprintto3d.domain.entity;

import com.blueprintto3d.domain.enum_class.ImgType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String userImage;
    private String fbxFile;

    @Enumerated(EnumType.STRING)
    private ImgType imgType;

    @JoinColumn(name = "user_no")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // AI fbx 파일 url 추가
    public void updateFbxFile(String fbxFile) {
        this.fbxFile = fbxFile;
    }

}
