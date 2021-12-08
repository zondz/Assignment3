package com.jsd.assignment3;

import com.jsd.assignment3.model.entity.Document;
import com.jsd.assignment3.model.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class Assignment3ApplicationTests {
	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	private TestEntityManager entityManager;
	@Test
	void testInsertFile() {
		//File file = new File();
		File file = new File("/Users/bia/Desktop/Screen Shot 2021-10-30 at 22.01.45.png");
		Document document = new Document();
		document.setName(file.getName());
		document.setFileSize(5.4f);

		Document savedDoc  = documentRepository.save(document);
		Document existDoc = entityManager.find(Document.class,savedDoc.getId());

	}

}
