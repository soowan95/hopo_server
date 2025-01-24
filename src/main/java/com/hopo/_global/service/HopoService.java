package com.hopo._global.service;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Service;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.entity.Hopo;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;
import com.hopo._utils.HopoStringUtils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 모드 Service 에서 공동으로 사용할 class
 * @param <E> Entity class
 * @param <ID> PK type
 */
@NoArgsConstructor(force = true)
@Service
@Slf4j
public class HopoService<E extends Hopo, ID> {

	private final HopoRepository<E, ID> repository;

	public HopoService(HopoRepository<E, ID> repository) {
		this.repository = repository;
	}

	/**
	 * 엔터티 저장
	 * @param request {@link HopoDto HopoDto}
	 * @return boolean
	 */
	public E save(HopoDto request) {
		try {
			assert repository != null;
			System.out.println(request.map(request));
			return repository.save((E)request.map(request));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 저장에 실패했습니다. \n Message: " + e.getMessage());
		}
	}

	/**
	 * 단건 조회
	 * @param request {@link HopoDto HopoDto} column 명
	 * @return entity
	 */
	public E show(HopoDto request) {
		Object[] args = request.get(0);
		assert repository != null;
		return repository.findByParam(args[0].toString(), args[1])
			.orElseThrow(() -> new HttpCodeHandleException("NO_SUCH_DATA"));
	}

	/**
	 * 전체 조회
	 * @return List
	 */
	public List<E> showAll() {
		assert repository != null;
		return repository.findAll();
	}

	/**
	 * 업데이트
	 * @param request {@link HopoDto HopoDto} 첫 번째 field 는 PK *
	 * @return boolean
	 */
	public E update(HopoDto request) {
		try {
			Object[] args = request.get(0);
			assert repository != null;
			E entity = repository.findByParam(args[0].toString(), args[1])
				.orElseThrow(() -> new HttpCodeHandleException("NO_SUCH_DATA"));
			return repository.save((E)request.map(entity, request));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 갱신 중 문제가 발생했습니다. \n Message: " + e.getMessage());
		}
	}

	/**
	 * 삭제
	 * @param request {@link HopoDto HopoDto} 첫 번째 field 는 PK *
	 * @return boolean
	 */
	public <D> boolean delete(HopoDto request) {
		try {
			repository.deleteById((ID)request.get(0, "value"));
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 삭제 중 문제가 발생했습니다. \n Message: " + e.getMessage());
		}
	}

	/**
	 * 중복된 데이터가 있는지 확인한다.
	 * @param field {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Boolean
	 */
	public Boolean checkDuplicate(String field, Object v) {
		String exceptionCode = switch (field) {
			case "code" -> "DUPLICATE_CODE";
			case "id" -> "DUPLICATE_ID";
			default -> "NO_SUCH_FIELD";
		};
		assert repository != null;
		repository.findByParam(field, v).ifPresent(e -> {
			throw new HttpCodeHandleException(exceptionCode);
		});
		return true;
	}
}
