package com.example.redis.service;

import com.example.redis.entity.Student;
import com.example.redis.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @CachePut(cacheNames = "student", key = "#student.studentId")
    public Student createStudent(Student student) {
        log.info("create in db");
        return studentRepository.save(student);
    }

    @Cacheable(cacheNames = "student", key = "#studentId")
    public Student getStudentById(Integer studentId) {
        log.info("fetch from db");
        return studentRepository.findById(studentId).orElse(null);
    }

    @CachePut(cacheNames = "student", key = "#student.studentId")
    public Student updateStudent(Student student) {
        log.info("update in db");
        if(studentRepository.existsById(student.getStudentId()))
            return studentRepository.save(student);
        else
            return null;
    }

    @CacheEvict(cacheNames = "student", key = "#studentId")
    public String deleteStudent(Integer studentId) {
        log.info("delete from db");
        studentRepository.deleteById(studentId);
        return "Deleted";
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
}
