<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="modaleDatiSospensioneQuota" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="headerDatiSospensioneQuota" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="headerDatiSospensioneQuota">Dati sospensione quota</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleDatiSospensioneQuota">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleDatiSospensioneQuota">
			<input type="hidden" name="sospensioneSubdocumento.uid" />
			<input type="hidden" name="indexSospensioneQuota" />
			<div class="control-group">
				<label class="control-label" for="dataSospensioneModaleDatiSospensioneQuota">Data</label>
				<div class="controls">
					<input type="text" id="dataSospensioneModaleDatiSospensioneQuota" class="span4 datepicker"
						name="sospensioneSubdocumento.dataSospensione" maxlength="10" placeholder="data sospensione" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="causaleSospensioneModaleDatiSospensioneQuota">Causale</label>
				<div class="controls">
					<select id="causaleSospensioneSelectModaleDatiSospensioneQuota" class="span4">
						<option value="0"></option>
						<s:iterator value="listaCausaleSospensione" var="cs">
							<option value="<s:property value="#cs.uid"/>" data-template="<s:property value="#cs.descrizione"/>"><s:property value="#cs.codice"/> - <s:property value="#cs.descrizione"/></option>
						</s:iterator>
					</select>
					&nbsp;
					<input type="text" id="causaleSospensioneModaleDatiSospensioneQuota" class="span7"
						name="sospensioneSubdocumento.causaleSospensione" placeholder="causale sospensione" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="dataRiattivazioneModaleDatiSospensioneQuota">Data&nbsp;
									<a href="#" class="tooltip-test" data-original-title="Per poter riattivare la quota è necessario effettuare la prima contabilizzazione.">
										<i class="icon-info-sign">&nbsp;
											<span class="nascosto">Per poter riattivare la quota è necessario effettuare la prima contabilizzazione.
											</span>
										</i>
									</a>
							
							</label>
				
				
				
				
				<div class="controls">
						<s:if test="editDateFromPccActive">
							<input type="text" id="dataRiattivazioneModaleDatiSospensioneQuota" class="span4 datepicker" name="sospensioneSubdocumento.dataRiattivazione" maxlength="10" placeholder="data riattivazione" />
						</s:if>
						<s:else>
							<input type="text" id="dataRiattivazioneModaleDatiSospensioneQuota" disabled class="span4 datepicker" name="sospensioneSubdocumento.dataRiattivazione" maxlength="10" placeholder="data riattivazione" />
							<s:hidden name="sospensioneSubdocumento.dataRiattivazione" />						
						</s:else>
						
				</div>
			
			
			</div>
		</fieldset>
	</div>

	<div class="modal-footer">
		<button type="button" class="btn btn-primary pull-right" id="pulsanteConfermaModaleDatiSospensioneQuota">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaModaleDatiSospensioneQuota"></i>
		</button>
	</div>
 
</div>