package com.hopo._global.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.hopo._global.dto.HopoDto;
import com.hopo._global.exception.HttpCodeHandleException;
import com.hopo._global.repository.HopoRepository;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 모드 Service 에서 공동으로 사용할 class
 * @param <E> Entity class
 * @param <ID> PK type
 */
@NoArgsConstructor(force = true)
@Slf4j
public class HopoService<E, ID> {

	private final HopoRepository<E, ID> repository;

	public HopoService(HopoRepository<E, ID> repository) {
		this.repository = repository;
	}

	/**
	 * 엔터티 저장
	 * @param request {@link HopoDto HopoDto} 를 상속받은 Class
	 * @return boolean
	 * @param <D> {@link HopoDto HopoDto} 를 상속받은 Class
	 */
	public <D> boolean save(D request) {
		try {
			// HopoDto 타입인지 확인
			if (request instanceof HopoDto<?, ?> dto) {
				// map 메서드 가져오기
				Method mapMethod = dto.getClass().getDeclaredMethod("map", dto.getClass());
				// map 메서드 실행
				E entity = (E) mapMethod.invoke(dto, dto);

				// 엔터티 저장
				assert repository != null;
				repository.save(entity);
				return true;
			}
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new HttpCodeHandleException(500, "데이터 저장에 실패했습니다." + e.getMessage());
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw new HttpCodeHandleException(500, "데이터 저장 중 예외 발생: " + cause.getMessage());
		}
		return false;
	}


	/**
	 * 단건 조회
	 * @param property {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return entity
	 */
	public E show(String property, Object v) {
		assert repository != null;
		return repository.findByParam(property, v)
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
	 * @param request {@link HopoDto HopoDto} 첫번째 field 는 PK *
	 * @return boolean
	 */
	public <D> boolean update(D request) {
		try {
			// HopoDto 타입인지 확인
			if (request instanceof HopoDto<?, ?> dto) {
				// get 메서드 가져오기
				Method getMethod = dto.getClass().getDeclaredMethod("get", Integer.class);
				// get 메서드 실행
				Object[] args = (Object[]) getMethod.invoke(dto, 0);
				assert repository != null;
				E entity = repository.findByParam(args[0].toString(), args[1])
					.orElseThrow(() -> new HttpCodeHandleException("NO_SUCH_DATA"));

				// map 메서드 가져오기
				Method mapMethod = dto.getClass().getDeclaredMethod("map", entity.getClass(), dto.getClass());
				// map 메서드 실행
				E updatedEntity = (E) mapMethod.invoke(dto, entity, dto);

				// 엔터티 저장
				repository.save(updatedEntity);
				return true;
			}
		} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			log.error("Cause : " + e.getCause() + ", msg : " + e.getMessage());
			throw new HttpCodeHandleException(500, "데이터 갱신 중 문제가 발생했습니다." + e.getMessage());
		}
		return false;
	}

	/**
	 * 중복된 데이터가 있는지 확인한다.
	 * @param property {@link String String} column 명
	 * @param v {@link Object Object} column 의 데이터 형을 특정할 수 없기 때문에 Object 타입으로 받는다
	 * @return Boolean
	 */
	public Boolean checkDuplicate(String property, Object v) {
		String exceptionCode = switch (property) {
			case "code" -> "DUPLICATE_CODE";
			case "id" -> "DUPLICATE_ID";
			default -> "NO_SUCH_PROPERTY";
		};
		assert repository != null;
		repository.findByParam(property, v).ifPresent(e -> {
			throw new HttpCodeHandleException(exceptionCode);
		});
		return true;
	}
}
