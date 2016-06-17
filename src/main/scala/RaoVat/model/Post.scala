package RaoVat.model

import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}

/**
  * Created by DucNa on 3/30/16.
  */
case class Post(id: Int,
                price: Double,
                updated_at: Int,
                created_at: Int,
                ward: Int,
                district: Int,
                city: Int,
                category_child: Int,
                category_parent: Int,
                attributes_cdx: Int,
                attributes_spt: Int,
                attributes_hn: Int,
                attributes_lnd: Int,
                attributes_st: Int,
                attributes_spn: Int)

object Post {

  implicit object PostReader extends BSONDocumentReader[Post] {
    def read(doc: BSONDocument): Post = {

      Post(
        id = if (doc.getAs[Int]("_id") != None) doc.getAs[Int]("_id").get else 0,
        price = if (doc.getAs[Long]("price") != None)
          doc.getAs[Long]("price").get
        else if (doc.getAs[Int]("price") != None)
          doc.getAs[Int]("price").get
        else 0,
        updated_at = if (doc.getAs[Int]("updated_at") != None) doc.getAs[Int]("updated_at").get else 0,
        created_at = if (doc.getAs[Int]("created_at") != None) doc.getAs[Int]("created_at").get else 0,
        ward = if (doc.getAs[BSONDocument]("location") != None)
          if (doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("ward") != None)
            if (doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("ward").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("ward").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        district = if (doc.getAs[BSONDocument]("location") != None)
          if (doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("district") != None)
            if (doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("district").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("district").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        city = if (doc.getAs[BSONDocument]("location") != None)
          if (doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("city") != None)
            if (doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("city").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("location").get.getAs[BSONDocument]("city").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        category_child = if (doc.getAs[BSONDocument]("category") != None)
          if (doc.getAs[BSONDocument]("category").get.getAs[BSONDocument]("child") != None)
            if (doc.getAs[BSONDocument]("category").get.getAs[BSONDocument]("child").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("category").get.getAs[BSONDocument]("child").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        category_parent = if (doc.getAs[BSONDocument]("category") != None)
          if (doc.getAs[BSONDocument]("category").get.getAs[BSONDocument]("parent") != None)
            if (doc.getAs[BSONDocument]("category").get.getAs[BSONDocument]("parent").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("category").get.getAs[BSONDocument]("parent").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        attributes_cdx = if (doc.getAs[BSONDocument]("attributes") != None)
          if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("cho_de_xe") != None)
            if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("cho_de_xe").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("cho_de_xe").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        attributes_spt = if (doc.getAs[BSONDocument]("attributes") != None)
          if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_phong_tam") != None)
            if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_phong_tam").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_phong_tam").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        attributes_hn = if (doc.getAs[BSONDocument]("attributes") != None)
          if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("huong_nha") != None)
            if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("huong_nha").get.getAs[Int]("id") != None)
              doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("huong_nha").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        attributes_lnd = if (doc.getAs[BSONDocument]("attributes") != None)
          if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("loai_nha_dat") != None)
            if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("loai_nha_dat") != None)
              doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("loai_nha_dat").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        attributes_st = if (doc.getAs[BSONDocument]("attributes") != None)
          if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_tang") != None)
            if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_tang") != None)
              doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_tang").get.getAs[Int]("id").get
            else 0
          else 0
        else 0,
        attributes_spn = if (doc.getAs[BSONDocument]("attributes") != None)
          if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_phong_ngu") != None)
            if (doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_phong_ngu") != None)
              doc.getAs[BSONDocument]("attributes").get.getAs[BSONDocument]("so_phong_ngu").get.getAs[Int]("id").get
            else 0
          else 0
        else 0
      )
    }

  }

}
