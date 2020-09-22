package com.github.codingdebugallday.service.impl;

import java.util.List;

import com.github.codingdebugallday.mapper.ResumeMapper;
import com.github.codingdebugallday.pojo.Resume;
import com.github.codingdebugallday.service.ResumeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/09/22 10:24
 * @since 1.0.0
 */
@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;

    public ResumeServiceImpl(ResumeMapper resumeMapper) {
        this.resumeMapper = resumeMapper;
    }

    @Override
    public List<Resume> list() {
        return resumeMapper.list();
    }

    @Override
    public Resume getById(Long id) {
        return resumeMapper.getById(id);
    }

    @Override
    public void insert(Resume resume) {
        resumeMapper.insert(resume);
    }

    @Override
    public void delete(Long id) {
        resumeMapper.delete(id);
    }

    @Override
    public void update(Resume resume) {
        resumeMapper.update(resume);
    }
}
